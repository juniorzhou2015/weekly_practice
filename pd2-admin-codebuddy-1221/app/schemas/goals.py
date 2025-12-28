from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel, Field


class ObjectiveBase(BaseModel):
    category: str = Field(min_length=1, max_length=32)
    period: str = Field(min_length=1, max_length=16)
    title: str = Field(min_length=1, max_length=200)
    description: str = ""
    owner: str = ""
    collaborator: str = ""
    priority: str = "P2"
    status: str = "进行中"
    progress: int = Field(default=0, ge=0, le=100)
    sort_order: int = 0


class ObjectiveCreate(ObjectiveBase):
    pass


class ObjectiveUpdate(BaseModel):
    period: str | None = Field(default=None, min_length=1, max_length=16)
    title: str | None = Field(default=None, min_length=1, max_length=200)
    description: str | None = None
    owner: str | None = None
    collaborator: str | None = None
    priority: str | None = None
    status: str | None = None
    progress: int | None = Field(default=None, ge=0, le=100)
    sort_order: int | None = None


class ObjectiveOut(ObjectiveBase):
    id: int
    created_at: datetime
    updated_at: datetime


class KeyResultBase(BaseModel):
    title: str = Field(min_length=1, max_length=200)
    description: str = ""
    owner: str = ""
    collaborator: str = ""
    priority: str = "P2"
    status: str = "进行中"
    progress: int = Field(default=0, ge=0, le=100)
    sort_order: int = 0


class KeyResultCreate(KeyResultBase):
    pass


class KeyResultUpdate(BaseModel):
    title: str | None = Field(default=None, min_length=1, max_length=200)
    description: str | None = None
    owner: str | None = None
    collaborator: str | None = None
    priority: str | None = None
    status: str | None = None
    progress: int | None = Field(default=None, ge=0, le=100)
    sort_order: int | None = None


class KeyResultOut(KeyResultBase):
    id: int
    objective_id: int
    created_at: datetime
    updated_at: datetime


class TaskBase(BaseModel):
    title: str = Field(min_length=1, max_length=200)
    description: str = ""
    owner: str = ""
    collaborator: str = ""
    priority: str = "P2"
    status: str = "进行中"
    progress: int = Field(default=0, ge=0, le=100)
    this_week_progress: str = ""
    attachments: list[str] = []
    sort_order: int = 0


class TaskCreate(TaskBase):
    pass


class TaskUpdate(BaseModel):
    title: str | None = Field(default=None, min_length=1, max_length=200)
    description: str | None = None
    owner: str | None = None
    collaborator: str | None = None
    priority: str | None = None
    status: str | None = None
    progress: int | None = Field(default=None, ge=0, le=100)
    this_week_progress: str | None = None
    attachments: list[str] | None = None
    sort_order: int | None = None


class TaskOut(TaskBase):
    id: int
    key_result_id: int
    created_at: datetime
    updated_at: datetime


class ObjectiveTree(BaseModel):
    objective: ObjectiveOut
    key_results: list["KeyResultTree"]


class KeyResultTree(BaseModel):
    key_result: KeyResultOut
    tasks: list[TaskOut]


ObjectiveTree.model_rebuild()
KeyResultTree.model_rebuild()


class SnapshotOut(BaseModel):
    id: int
    category: str
    period: str
    created_at: datetime


class SnapshotDetailOut(SnapshotOut):
    payload: dict


class EvaluationOut(BaseModel):
    owner: str
    period: str
    category: str
    generated_at: datetime
    summary: str
    highlights: list[str]
    risks: list[str]

