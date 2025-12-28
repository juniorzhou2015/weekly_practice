from __future__ import annotations

import json
import os
from pathlib import Path
from typing import Any

from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_prefix="PD2_", extra="ignore")

    # App
    app_title: str = "PD2 Portal"
    cors_origins: list[str] = ["*"]

    # DB
    mysql_dsn: str = "mysql+asyncmy://pd2:pd2@mysql:3306/pd2_portal?charset=utf8mb4"

    # Storage
    uploads_dir: str = "uploads"
    max_upload_mb: int = 50


def _read_config_json() -> dict[str, Any]:
    # Priority: env > config.json > defaults
    config_path = Path(__file__).resolve().parents[1] / "config.json"
    if not config_path.exists():
        return {}
    try:
        return json.loads(config_path.read_text(encoding="utf-8"))
    except Exception:
        return {}


def get_settings() -> Settings:
    cfg = _read_config_json()
    data: dict[str, Any] = {}
    if isinstance(cfg.get("app"), dict):
        data["app_title"] = cfg["app"].get("title", data.get("app_title"))
        data["cors_origins"] = cfg["app"].get("cors_origins", data.get("cors_origins"))
    if isinstance(cfg.get("db"), dict):
        data["mysql_dsn"] = cfg["db"].get("mysql_dsn", data.get("mysql_dsn"))
    if isinstance(cfg.get("storage"), dict):
        data["uploads_dir"] = cfg["storage"].get("uploads_dir", data.get("uploads_dir"))
        data["max_upload_mb"] = cfg["storage"].get("max_upload_mb", data.get("max_upload_mb"))

    # Environment variables override everything (pydantic-settings)
    env = os.environ
    if "PD2_CORS_ORIGINS" in env:
        # Allow passing JSON array or comma-separated.
        raw = env.get("PD2_CORS_ORIGINS", "")
        try:
            data["cors_origins"] = json.loads(raw)
        except Exception:
            data["cors_origins"] = [s.strip() for s in raw.split(",") if s.strip()]

    return Settings(**data)

