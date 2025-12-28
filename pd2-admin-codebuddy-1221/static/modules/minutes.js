import { renderCrudPage } from "/static/modules/crud.js";

export async function renderMinutes(pageEl) {
  return renderCrudPage(pageEl, {
    title: "会议纪要",
    description: "用于记录会议结论与待办（支持增删改查）",
    listPath: "/api/minutes",
    createPath: "/api/minutes",
    updatePath: (id) => `/api/minutes/${id}`,
    deletePath: (id) => `/api/minutes/${id}`,
    columns: [
      { key: "meeting_date", label: "日期" },
      { key: "title", label: "标题" },
      { key: "content", label: "内容", multiline: true },
    ],
    formFields: [
      { key: "meeting_date", label: "日期", type: "date" },
      { key: "title", label: "标题", full: true },
      { key: "content", label: "内容", type: "textarea", full: true, rows: 10 },
    ],
  });
}

