from __future__ import annotations

from datetime import date, datetime

from pydantic import BaseModel, Field


class DutyEventBase(BaseModel):
    title: str = Field(min_length=1, max_length=200)
    description: str = ""
    event_date: date
    sort_order: int = 0


class DutyEventCreate(DutyEventBase):
    pass


class DutyEventUpdate(BaseModel):
    title: str | None = Field(default=None, min_length=1, max_length=200)
    description: str | None = None
    event_date: date | None = None
    sort_order: int | None = None


class DutyEventOut(DutyEventBase):
    id: int
    created_at: datetime
    updated_at: datetime


class NextEventOut(BaseModel):
    event: DutyEventOut | None
    days_to_next: int | None
    highlight: bool


class DutyScheduleBase(BaseModel):
    week_start: date
    week_end: date
    person: str = Field(min_length=1, max_length=64)
    note: str = ""
    sort_order: int = 0


class DutyScheduleCreate(DutyScheduleBase):
    pass


class DutyScheduleUpdate(BaseModel):
    week_start: date | None = None
    week_end: date | None = None
    person: str | None = Field(default=None, min_length=1, max_length=64)
    note: str | None = None
    sort_order: int | None = None


class DutyScheduleOut(DutyScheduleBase):
    id: int
    created_at: datetime
    updated_at: datetime

