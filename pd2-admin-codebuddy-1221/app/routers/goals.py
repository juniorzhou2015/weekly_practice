from __future__ import annotations

from datetime import date, datetime, timedelta

from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.db import get_db
from app.models.goals import GoalSnapshot, KeyResult, Objective, TaskItem
from app.schemas.goals import (
    EvaluationOut,
    KeyResultCreate,
    KeyResultOut,
    KeyResultUpdate,
    ObjectiveCreate,
    ObjectiveOut,
    ObjectiveUpdate,
    SnapshotDetailOut,
    SnapshotOut,
    TaskCreate,
    TaskOut,
    TaskUpdate,
)
from app.services.goals import build_objective_tree, default_periods, generate_evaluation

router = APIRouter(prefix="/api/goals", tags=["goals"])


ALLOWED_CATEGORIES = {"quality", "architecture", "ai", "product"}


def _ensure_category(category: str) -> str:
    if category not in ALLOWED_CATEGORIES:
        raise HTTPException(status_code=400, detail="Invalid category")
    return category


def _not_found(name: str) -> HTTPException:
    return HTTPException(status_code=404, detail=f"{name} not found")


@router.get("/periods")
async def list_periods():
    return {"periods": default_periods()}


@router.get("/{category}/objectives", response_model=list[ObjectiveOut])
async def list_objectives(category: str, period: str = Query(...), db: AsyncSession = Depends(get_db)):
    category = _ensure_category(category)
    res = await db.execute(
        select(Objective)
        .where(Objective.category == category, Objective.period == period)
        .order_by(Objective.sort_order, Objective.id)
    )
    items = res.scalars().all()
    return [ObjectiveOut.model_validate(x, from_attributes=True) for x in items]


@router.post("/{category}/objectives", response_model=ObjectiveOut)
async def create_objective(category: str, payload: ObjectiveCreate, db: AsyncSession = Depends(get_db)):
    category = _ensure_category(category)
    data = payload.model_dump()
    data["category"] = category
    obj = Objective(**data)
    db.add(obj)
    await db.commit()
    await db.refresh(obj)
    return ObjectiveOut.model_validate(obj, from_attributes=True)


@router.put("/{category}/objectives/{objective_id}", response_model=ObjectiveOut)
async def update_objective(
    category: str, objective_id: int, payload: ObjectiveUpdate, db: AsyncSession = Depends(get_db)
):
    _ensure_category(category)
    obj = await db.get(Objective, objective_id)
    if not obj or obj.category != category:
        raise _not_found("Objective")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(obj, k, v)
    await db.commit()
    await db.refresh(obj)
    return ObjectiveOut.model_validate(obj, from_attributes=True)


@router.delete("/{category}/objectives/{objective_id}")
async def delete_objective(category: str, objective_id: int, db: AsyncSession = Depends(get_db)):
    _ensure_category(category)
    obj = await db.get(Objective, objective_id)
    if not obj or obj.category != category:
        raise _not_found("Objective")
    await db.delete(obj)
    await db.commit()
    return {"ok": True}


@router.get("/{category}/objectives/{objective_id}/key-results", response_model=list[KeyResultOut])
async def list_key_results(category: str, objective_id: int, db: AsyncSession = Depends(get_db)):
    _ensure_category(category)
    obj = await db.get(Objective, objective_id)
    if not obj or obj.category != category:
        raise _not_found("Objective")
    res = await db.execute(
        select(KeyResult).where(KeyResult.objective_id == objective_id).order_by(KeyResult.sort_order, KeyResult.id)
    )
    items = res.scalars().all()
    return [KeyResultOut.model_validate(x, from_attributes=True) for x in items]


@router.post("/{category}/objectives/{objective_id}/key-results", response_model=KeyResultOut)
async def create_key_result(category: str, objective_id: int, payload: KeyResultCreate, db: AsyncSession = Depends(get_db)):
    _ensure_category(category)
    obj = await db.get(Objective, objective_id)
    if not obj or obj.category != category:
        raise _not_found("Objective")
    kr = KeyResult(objective_id=objective_id, **payload.model_dump())
    db.add(kr)
    await db.commit()
    await db.refresh(kr)
    return KeyResultOut.model_validate(kr, from_attributes=True)


@router.put("/{category}/key-results/{kr_id}", response_model=KeyResultOut)
async def update_key_result(category: str, kr_id: int, payload: KeyResultUpdate, db: AsyncSession = Depends(get_db)):
    _ensure_category(category)
    kr = await db.get(KeyResult, kr_id)
    if not kr:
        raise _not_found("KeyResult")
    obj = await db.get(Objective, kr.objective_id)
    if not obj or obj.category != category:
        raise _not_found("KeyResult")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(kr, k, v)
    await db.commit()
    await db.refresh(kr)
    return KeyResultOut.model_validate(kr, from_attributes=True)


@router.delete("/{category}/key-results/{kr_id}")
async def delete_key_result(category: str, kr_id: int, db: AsyncSession = Depends(get_db)):
    _ensure_category(category)
    kr = await db.get(KeyResult, kr_id)
    if not kr:
        raise _not_found("KeyResult")
    obj = await db.get(Objective, kr.objective_id)
    if not obj or obj.category != category:
        raise _not_found("KeyResult")
    await db.delete(kr)
    await db.commit()
    return {"ok": True}


@router.get("/{category}/key-results/{kr_id}/tasks", response_model=list[TaskOut])
async def list_tasks(category: str, kr_id: int, db: AsyncSession = Depends(get_db)):
    _ensure_category(category)
    kr = await db.get(KeyResult, kr_id)
    if not kr:
        raise _not_found("KeyResult")
    obj = await db.get(Objective, kr.objective_id)
    if not obj or obj.category != category:
        raise _not_found("KeyResult")

    res = await db.execute(select(TaskItem).where(TaskItem.key_result_id == kr_id).order_by(TaskItem.sort_order, TaskItem.id))
    items = res.scalars().all()
    return [TaskOut.model_validate(x, from_attributes=True) for x in items]


@router.post("/{category}/key-results/{kr_id}/tasks", response_model=TaskOut)
async def create_task(category: str, kr_id: int, payload: TaskCreate, db: AsyncSession = Depends(get_db)):
    _ensure_category(category)
    kr = await db.get(KeyResult, kr_id)
    if not kr:
        raise _not_found("KeyResult")
    obj = await db.get(Objective, kr.objective_id)
    if not obj or obj.category != category:
        raise _not_found("KeyResult")

    task = TaskItem(key_result_id=kr_id, **payload.model_dump())
    db.add(task)
    await db.commit()
    await db.refresh(task)
    return TaskOut.model_validate(task, from_attributes=True)


@router.put("/{category}/tasks/{task_id}", response_model=TaskOut)
async def update_task(category: str, task_id: int, payload: TaskUpdate, db: AsyncSession = Depends(get_db)):
    _ensure_category(category)
    task = await db.get(TaskItem, task_id)
    if not task:
        raise _not_found("Task")
    kr = await db.get(KeyResult, task.key_result_id)
    obj = await db.get(Objective, kr.objective_id) if kr else None
    if not obj or obj.category != category:
        raise _not_found("Task")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(task, k, v)
    await db.commit()
    await db.refresh(task)
    return TaskOut.model_validate(task, from_attributes=True)


@router.delete("/{category}/tasks/{task_id}")
async def delete_task(category: str, task_id: int, db: AsyncSession = Depends(get_db)):
    _ensure_category(category)
    task = await db.get(TaskItem, task_id)
    if not task:
        raise _not_found("Task")
    kr = await db.get(KeyResult, task.key_result_id)
    obj = await db.get(Objective, kr.objective_id) if kr else None
    if not obj or obj.category != category:
        raise _not_found("Task")
    await db.delete(task)
    await db.commit()
    return {"ok": True}


@router.get("/{category}/tree")
async def get_tree(category: str, period: str = Query(...), db: AsyncSession = Depends(get_db)):
    category = _ensure_category(category)
    tree = await build_objective_tree(db, category=category, period=period)
    return {"items": tree}


@router.get("/{category}/weekly")
async def weekly_view(
    category: str,
    period: str = Query(...),
    since: date | None = Query(default=None, description="YYYY-MM-DD; default recent 14 days"),
    db: AsyncSession = Depends(get_db),
):
    category = _ensure_category(category)
    if since is None:
        since = date.today() - timedelta(days=14)

    # Join Task -> KR -> Objective
    stmt = (
        select(TaskItem, KeyResult, Objective)
        .join(KeyResult, TaskItem.key_result_id == KeyResult.id)
        .join(Objective, KeyResult.objective_id == Objective.id)
        .where(
            Objective.category == category,
            Objective.period == period,
            TaskItem.updated_at >= datetime.combine(since, datetime.min.time()),
        )
        .order_by(TaskItem.updated_at.desc())
    )
    res = await db.execute(stmt)
    rows = res.all()

    grouped: dict[str, list[dict]] = {}
    for task, kr, obj in rows:
        owner = task.owner or "未指定"
        grouped.setdefault(owner, []).append(
            {
                "objective": {"id": obj.id, "title": obj.title},
                "key_result": {"id": kr.id, "title": kr.title},
                "task": TaskOut.model_validate(task, from_attributes=True).model_dump(),
            }
        )

    return {"since": since.isoformat(), "owners": [{"owner": k, "items": v} for k, v in grouped.items()]}


@router.get("/{category}/personal")
async def personal_view(
    category: str,
    period: str = Query(...),
    owner: str = Query(...),
    db: AsyncSession = Depends(get_db),
):
    category = _ensure_category(category)
    stmt = (
        select(TaskItem, KeyResult, Objective)
        .join(KeyResult, TaskItem.key_result_id == KeyResult.id)
        .join(Objective, KeyResult.objective_id == Objective.id)
        .where(Objective.category == category, Objective.period == period, TaskItem.owner == owner)
        .order_by(TaskItem.updated_at.desc())
    )
    res = await db.execute(stmt)
    rows = res.all()
    items = []
    for task, kr, obj in rows:
        items.append(
            {
                "objective": {"id": obj.id, "title": obj.title},
                "key_result": {"id": kr.id, "title": kr.title},
                "task": TaskOut.model_validate(task, from_attributes=True).model_dump(),
            }
        )
    return {"owner": owner, "items": items}


@router.post("/{category}/snapshots", response_model=SnapshotOut)
async def create_snapshot(category: str, period: str = Query(...), db: AsyncSession = Depends(get_db)):
    category = _ensure_category(category)
    payload = {"items": await build_objective_tree(db, category=category, period=period)}
    snap = GoalSnapshot(category=category, period=period, payload=payload)
    db.add(snap)
    await db.commit()
    await db.refresh(snap)
    return SnapshotOut.model_validate(snap, from_attributes=True)


@router.get("/{category}/snapshots", response_model=list[SnapshotOut])
async def list_snapshots(category: str, period: str = Query(...), db: AsyncSession = Depends(get_db)):
    category = _ensure_category(category)
    res = await db.execute(
        select(GoalSnapshot)
        .where(GoalSnapshot.category == category, GoalSnapshot.period == period)
        .order_by(GoalSnapshot.created_at.desc(), GoalSnapshot.id.desc())
    )
    items = res.scalars().all()
    return [SnapshotOut.model_validate(x, from_attributes=True) for x in items]


@router.get("/{category}/snapshots/{snapshot_id}", response_model=SnapshotDetailOut)
async def snapshot_detail(category: str, snapshot_id: int, db: AsyncSession = Depends(get_db)):
    category = _ensure_category(category)
    snap = await db.get(GoalSnapshot, snapshot_id)
    if not snap or snap.category != category:
        raise _not_found("Snapshot")
    return SnapshotDetailOut.model_validate(snap, from_attributes=True)


@router.get("/{category}/evaluation", response_model=EvaluationOut)
async def evaluation(
    category: str,
    period: str = Query(...),
    owner: str = Query(...),
    db: AsyncSession = Depends(get_db),
):
    category = _ensure_category(category)
    stmt = (
        select(TaskItem)
        .join(KeyResult, TaskItem.key_result_id == KeyResult.id)
        .join(Objective, KeyResult.objective_id == Objective.id)
        .where(Objective.category == category, Objective.period == period, TaskItem.owner == owner)
    )
    res = await db.execute(stmt)
    tasks = res.scalars().all()
    out = generate_evaluation(owner=owner, period=period, category=category, tasks=tasks)
    # Pydantic response_model expects datetime, convert
    out["generated_at"] = datetime.fromisoformat(out["generated_at"])
    return EvaluationOut(**out)

