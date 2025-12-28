from __future__ import annotations

import asyncio
from contextlib import asynccontextmanager
from pathlib import Path

from fastapi import FastAPI
from fastapi import HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse
from fastapi.staticfiles import StaticFiles

from app.db import engine
from app.models import Base  # noqa: F401 (import side effects for metadata)
from app.routers import (
    duty_router,
    experience_router,
    goals_router,
    health_router,
    home_router,
    media_router,
    minutes_router,
)
from app.settings import get_settings


@asynccontextmanager
async def lifespan(app: FastAPI):
    # Minimal complexity: auto-create tables on boot.
    last_err: Exception | None = None
    for _ in range(10):
        try:
            async with engine.begin() as conn:
                await conn.run_sync(Base.metadata.create_all)
            last_err = None
            break
        except Exception as e:
            last_err = e
            await asyncio.sleep(2)
    if last_err is not None:
        raise last_err
    yield


settings = get_settings()
app = FastAPI(title=settings.app_title, lifespan=lifespan)

app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.cors_origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

ROOT = Path(__file__).resolve().parent
STATIC_DIR = ROOT / "static"
UPLOADS_DIR = ROOT / settings.uploads_dir

UPLOADS_DIR.mkdir(parents=True, exist_ok=True)

app.mount("/static", StaticFiles(directory=str(STATIC_DIR)), name="static")
app.mount("/uploads", StaticFiles(directory=str(UPLOADS_DIR)), name="uploads")

app.include_router(health_router)
app.include_router(media_router)
app.include_router(home_router)
app.include_router(goals_router)
app.include_router(duty_router)
app.include_router(experience_router)
app.include_router(minutes_router)


@app.get("/")
async def index():
    return FileResponse(str(STATIC_DIR / "index.html"))


@app.get("/{full_path:path}")
async def spa_fallback(full_path: str):
    # Keep refresh state for frontend routes:
    # - Do NOT override /api, /static, /uploads
    if full_path.startswith(("api/", "static/", "uploads/")):
        raise HTTPException(status_code=404, detail="Not Found")
    return FileResponse(str(STATIC_DIR / "index.html"))

