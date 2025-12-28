from __future__ import annotations

from collections import defaultdict
from datetime import date, datetime, timezone
from typing import Any

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.goals import KeyResult, Objective, TaskItem


def default_periods() -> list[str]:
    # Minimal hardcoded set; frontend can extend. Format: YY-H1 / YY-H2
    today = date.today()
    yy = today.year % 100
    return [f"{yy}-H2", f"{yy+1}-H1", f"{yy+1}-H2"]


async def build_objective_tree(db: AsyncSession, *, category: str, period: str) -> list[dict[str, Any]]:
    res = await db.execute(
        select(Objective)
        .where(Objective.category == category, Objective.period == period)
        .order_by(Objective.sort_order, Objective.id)
    )
    objectives = res.scalars().all()

    if not objectives:
        return []

    obj_ids = [o.id for o in objectives]
    res = await db.execute(
        select(KeyResult)
        .where(KeyResult.objective_id.in_(obj_ids))
        .order_by(KeyResult.sort_order, KeyResult.id)
    )
    krs = res.scalars().all()
    kr_ids = [k.id for k in krs]

    tasks_by_kr: dict[int, list[TaskItem]] = defaultdict(list)
    if kr_ids:
        res = await db.execute(
            select(TaskItem).where(TaskItem.key_result_id.in_(kr_ids)).order_by(TaskItem.sort_order, TaskItem.id)
        )
        tasks = res.scalars().all()
        for t in tasks:
            tasks_by_kr[t.key_result_id].append(t)

    krs_by_obj: dict[int, list[KeyResult]] = defaultdict(list)
    for kr in krs:
        krs_by_obj[kr.objective_id].append(kr)

    tree: list[dict[str, Any]] = []
    for obj in objectives:
        kr_nodes: list[dict[str, Any]] = []
        for kr in krs_by_obj.get(obj.id, []):
            kr_nodes.append(
                {
                    "key_result": _kr_dict(kr),
                    "tasks": [_task_dict(t) for t in tasks_by_kr.get(kr.id, [])],
                }
            )
        tree.append({"objective": _obj_dict(obj), "key_results": kr_nodes})
    return tree


def _obj_dict(o: Objective) -> dict[str, Any]:
    return {
        "id": o.id,
        "category": o.category,
        "period": o.period,
        "title": o.title,
        "description": o.description,
        "owner": o.owner,
        "collaborator": o.collaborator,
        "priority": o.priority,
        "status": o.status,
        "progress": o.progress,
        "sort_order": o.sort_order,
        "created_at": o.created_at.isoformat() if o.created_at else None,
        "updated_at": o.updated_at.isoformat() if o.updated_at else None,
    }


def _kr_dict(kr: KeyResult) -> dict[str, Any]:
    return {
        "id": kr.id,
        "objective_id": kr.objective_id,
        "title": kr.title,
        "description": kr.description,
        "owner": kr.owner,
        "collaborator": kr.collaborator,
        "priority": kr.priority,
        "status": kr.status,
        "progress": kr.progress,
        "sort_order": kr.sort_order,
        "created_at": kr.created_at.isoformat() if kr.created_at else None,
        "updated_at": kr.updated_at.isoformat() if kr.updated_at else None,
    }


def _task_dict(t: TaskItem) -> dict[str, Any]:
    return {
        "id": t.id,
        "key_result_id": t.key_result_id,
        "title": t.title,
        "description": t.description,
        "owner": t.owner,
        "collaborator": t.collaborator,
        "priority": t.priority,
        "status": t.status,
        "progress": t.progress,
        "this_week_progress": t.this_week_progress,
        "attachments": t.attachments or [],
        "sort_order": t.sort_order,
        "created_at": t.created_at.isoformat() if t.created_at else None,
        "updated_at": t.updated_at.isoformat() if t.updated_at else None,
    }


def generate_evaluation(*, owner: str, period: str, category: str, tasks: list[TaskItem]) -> dict[str, Any]:
    total = len(tasks)
    completed = 0
    highlights: list[str] = []
    risks: list[str] = []

    for t in tasks:
        done = t.progress >= 100 or ("完成" in (t.status or ""))
        if done:
            completed += 1
            if t.this_week_progress.strip():
                highlights.append(f"{t.title}：{t.this_week_progress.strip()[:120]}")
            else:
                highlights.append(f"{t.title}：已完成")
        else:
            if t.priority in {"P0", "P1"}:
                risks.append(f"{t.title}（{t.priority}）进度 {t.progress}% / 状态：{t.status}")

    ratio = (completed / total * 100) if total else 0
    summary = f"{owner} 在 {category}/{period} 共跟进 {total} 个任务，完成 {completed} 个，完成率 {ratio:.0f}%。"

    return {
        "owner": owner,
        "period": period,
        "category": category,
        "generated_at": datetime.now(timezone.utc).isoformat(),
        "summary": summary,
        "highlights": highlights[:10],
        "risks": risks[:10],
    }

