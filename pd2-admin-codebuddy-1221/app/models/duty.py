from __future__ import annotations

from datetime import date

from sqlalchemy import Date, Integer, String, Text
from sqlalchemy.orm import Mapped, mapped_column

from app.models.base import Base, SortableMixin, TimestampMixin


class DutyEvent(Base, TimestampMixin, SortableMixin):
    __tablename__ = "duty_events"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    title: Mapped[str] = mapped_column(String(200), nullable=False)
    description: Mapped[str] = mapped_column(Text, nullable=False, default="")
    event_date: Mapped[date] = mapped_column(Date, nullable=False)


class DutySchedule(Base, TimestampMixin, SortableMixin):
    __tablename__ = "duty_schedules"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    week_start: Mapped[date] = mapped_column(Date, nullable=False)  # Monday recommended
    week_end: Mapped[date] = mapped_column(Date, nullable=False)
    person: Mapped[str] = mapped_column(String(64), nullable=False)
    note: Mapped[str] = mapped_column(String(200), nullable=False, default="")

