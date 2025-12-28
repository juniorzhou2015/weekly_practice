from __future__ import annotations

from datetime import date

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.db import get_db
from app.models.home import ArchitectureModule, BusinessBoard, TeamMember, TeamMomentMedia
from app.schemas.home import (
    ArchitectureModuleCreate,
    ArchitectureModuleOut,
    ArchitectureModuleUpdate,
    BusinessBoardCreate,
    BusinessBoardOut,
    BusinessBoardUpdate,
    TeamMemberCreate,
    TeamMemberOut,
    TeamMemberUpdate,
    TeamMomentMediaCreate,
    TeamMomentMediaOut,
    TeamMomentMediaUpdate,
)

router = APIRouter(prefix="/api/home", tags=["home"])


def _not_found(name: str = "Item") -> HTTPException:
    return HTTPException(status_code=404, detail=f"{name} not found")


@router.get("/business-boards", response_model=list[BusinessBoardOut])
async def list_business_boards(db: AsyncSession = Depends(get_db)):
    res = await db.execute(select(BusinessBoard).order_by(BusinessBoard.sort_order, BusinessBoard.id))
    items = res.scalars().all()
    return [BusinessBoardOut.model_validate(x, from_attributes=True) for x in items]


@router.post("/business-boards", response_model=BusinessBoardOut)
async def create_business_board(payload: BusinessBoardCreate, db: AsyncSession = Depends(get_db)):
    obj = BusinessBoard(**payload.model_dump())
    db.add(obj)
    await db.commit()
    await db.refresh(obj)
    return BusinessBoardOut.model_validate(obj, from_attributes=True)


@router.put("/business-boards/{item_id}", response_model=BusinessBoardOut)
async def update_business_board(item_id: int, payload: BusinessBoardUpdate, db: AsyncSession = Depends(get_db)):
    obj = await db.get(BusinessBoard, item_id)
    if not obj:
        raise _not_found("BusinessBoard")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(obj, k, v)
    await db.commit()
    await db.refresh(obj)
    return BusinessBoardOut.model_validate(obj, from_attributes=True)


@router.delete("/business-boards/{item_id}")
async def delete_business_board(item_id: int, db: AsyncSession = Depends(get_db)):
    obj = await db.get(BusinessBoard, item_id)
    if not obj:
        raise _not_found("BusinessBoard")
    await db.delete(obj)
    await db.commit()
    return {"ok": True}


@router.get("/team-members", response_model=list[TeamMemberOut])
async def list_team_members(db: AsyncSession = Depends(get_db)):
    res = await db.execute(select(TeamMember).order_by(TeamMember.sort_order, TeamMember.id))
    items = res.scalars().all()
    return [TeamMemberOut.model_validate(x, from_attributes=True) for x in items]


@router.get("/team-members/birthday-today", response_model=list[TeamMemberOut])
async def birthday_today(db: AsyncSession = Depends(get_db)):
    today = date.today()
    res = await db.execute(select(TeamMember).where(TeamMember.birthday.is_not(None)))
    items = res.scalars().all()
    matched = [x for x in items if x.birthday and x.birthday.month == today.month and x.birthday.day == today.day]
    matched.sort(key=lambda x: (x.sort_order, x.id))
    return [TeamMemberOut.model_validate(x, from_attributes=True) for x in matched]


@router.post("/team-members", response_model=TeamMemberOut)
async def create_team_member(payload: TeamMemberCreate, db: AsyncSession = Depends(get_db)):
    obj = TeamMember(**payload.model_dump())
    db.add(obj)
    await db.commit()
    await db.refresh(obj)
    return TeamMemberOut.model_validate(obj, from_attributes=True)


@router.put("/team-members/{item_id}", response_model=TeamMemberOut)
async def update_team_member(item_id: int, payload: TeamMemberUpdate, db: AsyncSession = Depends(get_db)):
    obj = await db.get(TeamMember, item_id)
    if not obj:
        raise _not_found("TeamMember")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(obj, k, v)
    await db.commit()
    await db.refresh(obj)
    return TeamMemberOut.model_validate(obj, from_attributes=True)


@router.delete("/team-members/{item_id}")
async def delete_team_member(item_id: int, db: AsyncSession = Depends(get_db)):
    obj = await db.get(TeamMember, item_id)
    if not obj:
        raise _not_found("TeamMember")
    await db.delete(obj)
    await db.commit()
    return {"ok": True}


@router.get("/architecture-modules", response_model=list[ArchitectureModuleOut])
async def list_arch_modules(db: AsyncSession = Depends(get_db)):
    res = await db.execute(select(ArchitectureModule).order_by(ArchitectureModule.sort_order, ArchitectureModule.id))
    items = res.scalars().all()
    return [ArchitectureModuleOut.model_validate(x, from_attributes=True) for x in items]


@router.post("/architecture-modules", response_model=ArchitectureModuleOut)
async def create_arch_module(payload: ArchitectureModuleCreate, db: AsyncSession = Depends(get_db)):
    obj = ArchitectureModule(**payload.model_dump())
    db.add(obj)
    await db.commit()
    await db.refresh(obj)
    return ArchitectureModuleOut.model_validate(obj, from_attributes=True)


@router.put("/architecture-modules/{item_id}", response_model=ArchitectureModuleOut)
async def update_arch_module(item_id: int, payload: ArchitectureModuleUpdate, db: AsyncSession = Depends(get_db)):
    obj = await db.get(ArchitectureModule, item_id)
    if not obj:
        raise _not_found("ArchitectureModule")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(obj, k, v)
    await db.commit()
    await db.refresh(obj)
    return ArchitectureModuleOut.model_validate(obj, from_attributes=True)


@router.delete("/architecture-modules/{item_id}")
async def delete_arch_module(item_id: int, db: AsyncSession = Depends(get_db)):
    obj = await db.get(ArchitectureModule, item_id)
    if not obj:
        raise _not_found("ArchitectureModule")
    await db.delete(obj)
    await db.commit()
    return {"ok": True}


@router.get("/team-moments", response_model=list[TeamMomentMediaOut])
async def list_team_moments(db: AsyncSession = Depends(get_db)):
    res = await db.execute(select(TeamMomentMedia).order_by(TeamMomentMedia.sort_order, TeamMomentMedia.id))
    items = res.scalars().all()
    return [TeamMomentMediaOut.model_validate(x, from_attributes=True) for x in items]


@router.post("/team-moments", response_model=TeamMomentMediaOut)
async def create_team_moment(payload: TeamMomentMediaCreate, db: AsyncSession = Depends(get_db)):
    obj = TeamMomentMedia(**payload.model_dump())
    db.add(obj)
    await db.commit()
    await db.refresh(obj)
    return TeamMomentMediaOut.model_validate(obj, from_attributes=True)


@router.put("/team-moments/{item_id}", response_model=TeamMomentMediaOut)
async def update_team_moment(item_id: int, payload: TeamMomentMediaUpdate, db: AsyncSession = Depends(get_db)):
    obj = await db.get(TeamMomentMedia, item_id)
    if not obj:
        raise _not_found("TeamMomentMedia")
    for k, v in payload.model_dump(exclude_unset=True).items():
        setattr(obj, k, v)
    await db.commit()
    await db.refresh(obj)
    return TeamMomentMediaOut.model_validate(obj, from_attributes=True)


@router.delete("/team-moments/{item_id}")
async def delete_team_moment(item_id: int, db: AsyncSession = Depends(get_db)):
    obj = await db.get(TeamMomentMedia, item_id)
    if not obj:
        raise _not_found("TeamMomentMedia")
    await db.delete(obj)
    await db.commit()
    return {"ok": True}

