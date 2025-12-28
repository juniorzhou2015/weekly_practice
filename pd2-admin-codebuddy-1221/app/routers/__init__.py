from app.routers.duty import router as duty_router
from app.routers.experience import router as experience_router
from app.routers.goals import router as goals_router
from app.routers.health import router as health_router
from app.routers.home import router as home_router
from app.routers.media import router as media_router
from app.routers.minutes import router as minutes_router

__all__ = [
    "health_router",
    "home_router",
    "goals_router",
    "duty_router",
    "experience_router",
    "minutes_router",
    "media_router",
]

