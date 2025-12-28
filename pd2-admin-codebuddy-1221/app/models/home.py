from __future__ import annotations

from datetime import date

from sqlalchemy import Date, Integer, String, Text
from sqlalchemy.orm import Mapped, mapped_column

from app.models.base import Base, SortableMixin, TimestampMixin


class BusinessBoard(Base, TimestampMixin, SortableMixin):
    __tablename__ = "home_business_boards"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    title: Mapped[str] = mapped_column(String(120), nullable=False)
    description: Mapped[str] = mapped_column(Text, nullable=False, default="")


class TeamMember(Base, TimestampMixin, SortableMixin):
    __tablename__ = "home_team_members"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    english_name: Mapped[str] = mapped_column(String(64), nullable=False)
    responsibility: Mapped[str] = mapped_column(String(200), nullable=False, default="")
    quality_partner: Mapped[str] = mapped_column(String(64), nullable=False, default="")
    phone: Mapped[str] = mapped_column(String(32), nullable=False, default="")
    email: Mapped[str] = mapped_column(String(120), nullable=False, default="")
    birthday: Mapped[date | None] = mapped_column(Date, nullable=True)


class ArchitectureModule(Base, TimestampMixin, SortableMixin):
    __tablename__ = "home_architecture_modules"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    title: Mapped[str] = mapped_column(String(120), nullable=False)
    # Store as URL (e.g. /uploads/xxx.png). Upload handled separately.
    diagram_url: Mapped[str] = mapped_column(String(500), nullable=False, default="")


class TeamMomentMedia(Base, TimestampMixin, SortableMixin):
    __tablename__ = "home_team_moments"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    media_type: Mapped[str] = mapped_column(String(16), nullable=False)  # image | video
    title: Mapped[str] = mapped_column(String(120), nullable=False, default="")
    url: Mapped[str] = mapped_column(String(500), nullable=False)  # /uploads/...

