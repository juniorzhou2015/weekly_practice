from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel, Field


class ExperienceEntryCreate(BaseModel):
    title: str = Field(min_length=1, max_length=200)
    content: str = ""
    sort_order: int = 0


class ExperienceEntryUpdate(BaseModel):
    title: str | None = Field(default=None, min_length=1, max_length=200)
    content: str | None = None
    sort_order: int | None = None


class ExperienceEntryOut(BaseModel):
    id: int
    category: str
    title: str
    content: str
    sort_order: int
    created_at: datetime
    updated_at: datetime

