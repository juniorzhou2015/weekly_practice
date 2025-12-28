from __future__ import annotations

import os
from pathlib import Path
from uuid import uuid4

import aiofiles
from fastapi import APIRouter, File, HTTPException, UploadFile

from app.settings import get_settings

router = APIRouter(prefix="/api/media", tags=["media"])


@router.post("/upload")
async def upload(file: UploadFile = File(...)):
    settings = get_settings()
    uploads_dir = Path(__file__).resolve().parents[2] / settings.uploads_dir
    uploads_dir.mkdir(parents=True, exist_ok=True)

    safe_name = (file.filename or "file").replace("/", "_").replace("\\", "_")
    ext = "".join(Path(safe_name).suffixes)[-20:]
    name = f"{uuid4().hex}{ext}"
    full_path = uploads_dir / name

    max_bytes = settings.max_upload_mb * 1024 * 1024
    written = 0
    try:
        async with aiofiles.open(full_path, "wb") as f:
            while True:
                chunk = await file.read(1024 * 1024)
                if not chunk:
                    break
                written += len(chunk)
                if written > max_bytes:
                    raise HTTPException(status_code=413, detail="File too large")
                await f.write(chunk)
    except Exception:
        if full_path.exists():
            os.remove(full_path)
        raise

    return {
        "ok": True,
        "filename": name,
        "content_type": file.content_type,
        "url": f"/uploads/{name}",
    }


@router.delete("/{filename}")
async def delete_upload(filename: str):
    settings = get_settings()
    uploads_dir = Path(__file__).resolve().parents[2] / settings.uploads_dir
    full_path = (uploads_dir / filename).resolve()
    if uploads_dir.resolve() not in full_path.parents:
        raise HTTPException(status_code=400, detail="Invalid filename")
    if not full_path.exists():
        raise HTTPException(status_code=404, detail="File not found")
    os.remove(full_path)
    return {"ok": True}

