from __future__ import annotations

from datetime import date

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.db import get_db
from app.models.duty import DutyEvent, DutySchedule
from app.schemas.duty import (
    DutyEventCreate,
    DutyEventOut,
    DutyEventUpdate,
    DutyScheduleCreate,
    DutyScheduleOut,
    DutyScheduleUpdate,
    NextEventOut,
)

router = APIRouter(prefix="/api/duty", tags=["duty"])


def _not_found(name: str) -> HTTPException:
    return HTTPException(status_code=404, detail=f"{name} not found")


@router.get("/events", response_model=list[DutyEventOut])
async def list_events(db: AsyncSession = Depends(get_db)):
    res = await db.execute(select(DutyEvent).order_by(DutyEvent.event_date.asc(), DutyEvent.sort_order, DutyEvent.id))
    items = res.scalars().all()
    return [DutyEventOut.model_validate(x, from_attributes=True) for x in items]


@router.get("/events/next", response_model=NextEventOut)
async def next_event(db: AsyncSession = Depends(get_db)):
    today = date.today()
    res = await db.execute(
        select(DutyEvent).where(DutyEvent.event_date >= today).order_by(DutyEvent.event_date.asc(), DutyEvent.id.asc())
    )
    ev = res.scalars().first()
    if not ev:
        return NextEventOut(event=None, days_to_next=None, highlight=False)
    days = (ev.event_date - today).days
    return NextEventOut(
        event=DutyEventOut.model_validate(ev, from_attributes=True),
        days_to_next=days,
        highlight=days <= 30,
    )


@router.post("/events", response_model=DutyEventOut)
async def create_event(payload: DutyEventCreate, db: AsyncSession = Depends(get_db)):
    obj = DutyEvent(**payload.model_dump())
    db.add(obj)
    await db.commit()
    await db.refresh(obj)
    return DutyEventOut.model_validate(obj, from_attributes=True)


@router.put("/events/{event_id}", response_model=DutyEventOut)
async def update_event(event_id: int, payload: DutyEventUpdate, db: AsyncSession = Depends(get_db)):
    obj = await db.get(DutyEvent, event_id)
    if not obj:
        raise _not_found("Event")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(obj, k, v)
    await db.commit()
    await db.refresh(obj)
    return DutyEventOut.model_validate(obj, from_attributes=True)


@router.delete("/events/{event_id}")
async def delete_event(event_id: int, db: AsyncSession = Depends(get_db)):
    obj = await db.get(DutyEvent, event_id)
    if not obj:
        raise _not_found("Event")
    await db.delete(obj)
    await db.commit()
    return {"ok": True}


@router.get("/schedules", response_model=list[DutyScheduleOut])
async def list_schedules(db: AsyncSession = Depends(get_db)):
    res = await db.execute(select(DutySchedule).order_by(DutySchedule.week_start.asc(), DutySchedule.sort_order, DutySchedule.id))
    items = res.scalars().all()
    return [DutyScheduleOut.model_validate(x, from_attributes=True) for x in items]


@router.post("/schedules", response_model=DutyScheduleOut)
async def create_schedule(payload: DutyScheduleCreate, db: AsyncSession = Depends(get_db)):
    obj = DutySchedule(**payload.model_dump())
    db.add(obj)
    await db.commit()
    await db.refresh(obj)
    return DutyScheduleOut.model_validate(obj, from_attributes=True)


@router.put("/schedules/{schedule_id}", response_model=DutyScheduleOut)
async def update_schedule(schedule_id: int, payload: DutyScheduleUpdate, db: AsyncSession = Depends(get_db)):
    obj = await db.get(DutySchedule, schedule_id)
    if not obj:
        raise _not_found("Schedule")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(obj, k, v)
    await db.commit()
    await db.refresh(obj)
    return DutyScheduleOut.model_validate(obj, from_attributes=True)


@router.delete("/schedules/{schedule_id}")
async def delete_schedule(schedule_id: int, db: AsyncSession = Depends(get_db)):
    obj = await db.get(DutySchedule, schedule_id)
    if not obj:
        raise _not_found("Schedule")
    await db.delete(obj)
    await db.commit()
    return {"ok": True}

