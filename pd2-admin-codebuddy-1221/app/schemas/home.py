from __future__ import annotations

from datetime import date, datetime

from pydantic import BaseModel, Field


class BusinessBoardBase(BaseModel):
    title: str = Field(min_length=1, max_length=120)
    description: str = ""
    sort_order: int = 0


class BusinessBoardCreate(BusinessBoardBase):
    pass


class BusinessBoardUpdate(BaseModel):
    title: str | None = Field(default=None, min_length=1, max_length=120)
    description: str | None = None
    sort_order: int | None = None


class BusinessBoardOut(BusinessBoardBase):
    id: int
    created_at: datetime
    updated_at: datetime


class TeamMemberBase(BaseModel):
    english_name: str = Field(min_length=1, max_length=64)
    responsibility: str = ""
    quality_partner: str = ""
    phone: str = ""
    email: str = ""
    birthday: date | None = None
    sort_order: int = 0


class TeamMemberCreate(TeamMemberBase):
    pass


class TeamMemberUpdate(BaseModel):
    english_name: str | None = Field(default=None, min_length=1, max_length=64)
    responsibility: str | None = None
    quality_partner: str | None = None
    phone: str | None = None
    email: str | None = None
    birthday: date | None = None
    sort_order: int | None = None


class TeamMemberOut(TeamMemberBase):
    id: int
    created_at: datetime
    updated_at: datetime


class ArchitectureModuleBase(BaseModel):
    title: str = Field(min_length=1, max_length=120)
    diagram_url: str = ""
    sort_order: int = 0


class ArchitectureModuleCreate(ArchitectureModuleBase):
    pass


class ArchitectureModuleUpdate(BaseModel):
    title: str | None = Field(default=None, min_length=1, max_length=120)
    diagram_url: str | None = None
    sort_order: int | None = None


class ArchitectureModuleOut(ArchitectureModuleBase):
    id: int
    created_at: datetime
    updated_at: datetime


class TeamMomentMediaBase(BaseModel):
    media_type: str = Field(pattern="^(image|video)$")
    title: str = ""
    url: str
    sort_order: int = 0


class TeamMomentMediaCreate(TeamMomentMediaBase):
    pass


class TeamMomentMediaUpdate(BaseModel):
    media_type: str | None = Field(default=None, pattern="^(image|video)$")
    title: str | None = None
    url: str | None = None
    sort_order: int | None = None


class TeamMomentMediaOut(TeamMomentMediaBase):
    id: int
    created_at: datetime
    updated_at: datetime

