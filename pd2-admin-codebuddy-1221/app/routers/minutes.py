from __future__ import annotations

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.db import get_db
from app.models.minutes import MeetingMinute
from app.schemas.minutes import MeetingMinuteCreate, MeetingMinuteOut, MeetingMinuteUpdate

router = APIRouter(prefix="/api/minutes", tags=["minutes"])


def _not_found(name: str) -> HTTPException:
    return HTTPException(status_code=404, detail=f"{name} not found")


@router.get("", response_model=list[MeetingMinuteOut])
async def list_minutes(db: AsyncSession = Depends(get_db)):
    res = await db.execute(select(MeetingMinute).order_by(MeetingMinute.meeting_date.desc(), MeetingMinute.sort_order, MeetingMinute.id))
    items = res.scalars().all()
    return [MeetingMinuteOut.model_validate(x, from_attributes=True) for x in items]


@router.post("", response_model=MeetingMinuteOut)
async def create_minute(payload: MeetingMinuteCreate, db: AsyncSession = Depends(get_db)):
    obj = MeetingMinute(**payload.model_dump())
    db.add(obj)
    await db.commit()
    await db.refresh(obj)
    return MeetingMinuteOut.model_validate(obj, from_attributes=True)


@router.put("/{minute_id}", response_model=MeetingMinuteOut)
async def update_minute(minute_id: int, payload: MeetingMinuteUpdate, db: AsyncSession = Depends(get_db)):
    obj = await db.get(MeetingMinute, minute_id)
    if not obj:
        raise _not_found("Minute")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(obj, k, v)
    await db.commit()
    await db.refresh(obj)
    return MeetingMinuteOut.model_validate(obj, from_attributes=True)


@router.delete("/{minute_id}")
async def delete_minute(minute_id: int, db: AsyncSession = Depends(get_db)):
    obj = await db.get(MeetingMinute, minute_id)
    if not obj:
        raise _not_found("Minute")
    await db.delete(obj)
    await db.commit()
    return {"ok": True}

