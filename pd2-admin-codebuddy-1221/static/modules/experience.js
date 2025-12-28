import { renderCrudPage } from "/static/modules/crud.js";

const CATEGORY_TITLES = {
  requirement_solution: "需求方案",
  incident_history: "历史故障",
  spring_festival: "春节保障",
  probing_alarm: "拨测告警",
  practice_thoughts: "实践思考",
};

export async function renderExperience(pageEl, sub) {
  const title = CATEGORY_TITLES[sub] || `经验沉淀：${sub}`;
  return renderCrudPage(pageEl, {
    title,
    description: "内容支持自动换行显示（支持增删改查）",
    listPath: `/api/experience/${sub}/entries`,
    createPath: `/api/experience/${sub}/entries`,
    updatePath: (id) => `/api/experience/entries/${id}`,
    deletePath: (id) => `/api/experience/entries/${id}`,
    columns: [
      { key: "title", label: "标题" },
      { key: "content", label: "内容", multiline: true },
    ],
    formFields: [
      { key: "title", label: "标题", full: true },
      { key: "content", label: "内容", type: "textarea", full: true, rows: 10 },
    ],
    mapCreatePayload: async (data) => {
      // backend ignores category from payload and uses path
      delete data.category;
      return data;
    },
  });
}

