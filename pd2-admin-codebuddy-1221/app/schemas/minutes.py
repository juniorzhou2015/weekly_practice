from __future__ import annotations

from datetime import date, datetime

from pydantic import BaseModel, Field


class MeetingMinuteBase(BaseModel):
    meeting_date: date
    title: str = Field(min_length=1, max_length=200)
    content: str = ""
    sort_order: int = 0


class MeetingMinuteCreate(MeetingMinuteBase):
    pass


class MeetingMinuteUpdate(BaseModel):
    meeting_date: date | None = None
    title: str | None = Field(default=None, min_length=1, max_length=200)
    content: str | None = None
    sort_order: int | None = None


class MeetingMinuteOut(MeetingMinuteBase):
    id: int
    created_at: datetime
    updated_at: datetime

