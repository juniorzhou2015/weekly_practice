from __future__ import annotations

from datetime import datetime

from sqlalchemy import DateTime, ForeignKey, Integer, JSON, String, Text, func
from sqlalchemy.orm import Mapped, mapped_column, relationship

from app.models.base import Base, SortableMixin, TimestampMixin


class Objective(Base, TimestampMixin, SortableMixin):
    __tablename__ = "goals_objectives"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    category: Mapped[str] = mapped_column(String(32), nullable=False)  # quality|architecture|ai|product
    period: Mapped[str] = mapped_column(String(16), nullable=False)  # e.g. 25-H2

    title: Mapped[str] = mapped_column(String(200), nullable=False)
    description: Mapped[str] = mapped_column(Text, nullable=False, default="")

    owner: Mapped[str] = mapped_column(String(64), nullable=False, default="")
    collaborator: Mapped[str] = mapped_column(String(64), nullable=False, default="")

    priority: Mapped[str] = mapped_column(String(16), nullable=False, default="P2")  # P0/P1/P2/P3
    status: Mapped[str] = mapped_column(String(32), nullable=False, default="进行中")
    progress: Mapped[int] = mapped_column(Integer, nullable=False, default=0)  # 0-100

    key_results: Mapped[list["KeyResult"]] = relationship(
        back_populates="objective",
        cascade="all, delete-orphan",
        passive_deletes=True,
        order_by="KeyResult.sort_order.asc()",
    )


class KeyResult(Base, TimestampMixin, SortableMixin):
    __tablename__ = "goals_key_results"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    objective_id: Mapped[int] = mapped_column(
        Integer,
        ForeignKey("goals_objectives.id", ondelete="CASCADE"),
        nullable=False,
    )

    title: Mapped[str] = mapped_column(String(200), nullable=False)
    description: Mapped[str] = mapped_column(Text, nullable=False, default="")

    owner: Mapped[str] = mapped_column(String(64), nullable=False, default="")
    collaborator: Mapped[str] = mapped_column(String(64), nullable=False, default="")
    priority: Mapped[str] = mapped_column(String(16), nullable=False, default="P2")
    status: Mapped[str] = mapped_column(String(32), nullable=False, default="进行中")
    progress: Mapped[int] = mapped_column(Integer, nullable=False, default=0)

    objective: Mapped["Objective"] = relationship(back_populates="key_results")
    tasks: Mapped[list["TaskItem"]] = relationship(
        back_populates="key_result",
        cascade="all, delete-orphan",
        passive_deletes=True,
        order_by="TaskItem.sort_order.asc()",
    )


class TaskItem(Base, SortableMixin):
    __tablename__ = "goals_tasks"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    key_result_id: Mapped[int] = mapped_column(
        Integer,
        ForeignKey("goals_key_results.id", ondelete="CASCADE"),
        nullable=False,
    )

    title: Mapped[str] = mapped_column(String(200), nullable=False)
    description: Mapped[str] = mapped_column(Text, nullable=False, default="")

    owner: Mapped[str] = mapped_column(String(64), nullable=False, default="")
    collaborator: Mapped[str] = mapped_column(String(64), nullable=False, default="")
    priority: Mapped[str] = mapped_column(String(16), nullable=False, default="P2")
    status: Mapped[str] = mapped_column(String(32), nullable=False, default="进行中")
    progress: Mapped[int] = mapped_column(Integer, nullable=False, default=0)
    this_week_progress: Mapped[str] = mapped_column(Text, nullable=False, default="")
    attachments: Mapped[list[str]] = mapped_column(JSON, nullable=False, default=list)

    created_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        nullable=False,
        server_default=func.now(),
    )
    updated_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        nullable=False,
        server_default=func.now(),
        onupdate=func.now(),
    )

    key_result: Mapped["KeyResult"] = relationship(back_populates="tasks")


class GoalSnapshot(Base):
    __tablename__ = "goals_snapshots"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    category: Mapped[str] = mapped_column(String(32), nullable=False)
    period: Mapped[str] = mapped_column(String(16), nullable=False)
    created_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        nullable=False,
        server_default=func.now(),
    )
    payload: Mapped[dict] = mapped_column(JSON, nullable=False)

