from __future__ import annotations

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.db import get_db
from app.models.experience import ExperienceEntry
from app.schemas.experience import ExperienceEntryCreate, ExperienceEntryOut, ExperienceEntryUpdate

router = APIRouter(prefix="/api/experience", tags=["experience"])


def _not_found(name: str) -> HTTPException:
    return HTTPException(status_code=404, detail=f"{name} not found")


@router.get("/{category}/entries", response_model=list[ExperienceEntryOut])
async def list_entries(category: str, db: AsyncSession = Depends(get_db)):
    res = await db.execute(
        select(ExperienceEntry)
        .where(ExperienceEntry.category == category)
        .order_by(ExperienceEntry.sort_order, ExperienceEntry.id)
    )
    items = res.scalars().all()
    return [ExperienceEntryOut.model_validate(x, from_attributes=True) for x in items]


@router.post("/{category}/entries", response_model=ExperienceEntryOut)
async def create_entry(category: str, payload: ExperienceEntryCreate, db: AsyncSession = Depends(get_db)):
    data = payload.model_dump()
    data["category"] = category
    obj = ExperienceEntry(**data)
    db.add(obj)
    await db.commit()
    await db.refresh(obj)
    return ExperienceEntryOut.model_validate(obj, from_attributes=True)


@router.put("/entries/{entry_id}", response_model=ExperienceEntryOut)
async def update_entry(entry_id: int, payload: ExperienceEntryUpdate, db: AsyncSession = Depends(get_db)):
    obj = await db.get(ExperienceEntry, entry_id)
    if not obj:
        raise _not_found("Entry")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(obj, k, v)
    await db.commit()
    await db.refresh(obj)
    return ExperienceEntryOut.model_validate(obj, from_attributes=True)


@router.delete("/entries/{entry_id}")
async def delete_entry(entry_id: int, db: AsyncSession = Depends(get_db)):
    obj = await db.get(ExperienceEntry, entry_id)
    if not obj:
        raise _not_found("Entry")
    await db.delete(obj)
    await db.commit()
    return {"ok": True}

