import { apiFetch, escapeHtml, toast } from "/static/utils.js";
import { renderCrudPage } from "/static/modules/crud.js";

export async function renderDuty(pageEl, sub) {
  if (sub === "events") {
    return renderCrudPage(pageEl, {
      title: "重要事件日历",
      description: "支持事件录入/查看/管理；距离下一个日程 <= 1 个月时会提醒高亮",
      listPath: "/api/duty/events",
      createPath: "/api/duty/events",
      updatePath: (id) => `/api/duty/events/${id}`,
      deletePath: (id) => `/api/duty/events/${id}`,
      columns: [
        { key: "event_date", label: "日期" },
        { key: "title", label: "标题" },
        { key: "description", label: "描述", multiline: true },
      ],
      formFields: [
        { key: "event_date", label: "日期", type: "date" },
        { key: "title", label: "标题", full: true },
        { key: "description", label: "描述", type: "textarea", full: true, rows: 5 },
      ],
      afterRender: async () => {
        try {
          const next = await apiFetch("/api/duty/events/next");
          if (!next?.event) return;
          if (next.highlight) {
            toast(`提醒：距离下一次日程还有 ${next.days_to_next} 天（${next.event.title}）`, { type: "info", ms: 3500 });
          }
        } catch (_) {}
      },
    });
  }

  if (sub === "schedules") {
    return renderCrudPage(pageEl, {
      title: "团队值班安排",
      description: "按周配置值班人员（支持增删改查）",
      listPath: "/api/duty/schedules",
      createPath: "/api/duty/schedules",
      updatePath: (id) => `/api/duty/schedules/${id}`,
      deletePath: (id) => `/api/duty/schedules/${id}`,
      columns: [
        { key: "week_start", label: "周起始" },
        { key: "week_end", label: "周结束" },
        { key: "person", label: "值班人员" },
        { key: "note", label: "备注", multiline: true },
      ],
      formFields: [
        { key: "week_start", label: "周起始", type: "date" },
        { key: "week_end", label: "周结束", type: "date" },
        { key: "person", label: "值班人员" },
        { key: "note", label: "备注", type: "textarea", full: true, rows: 4 },
      ],
    });
  }

  pageEl.innerHTML = `<div class="text-slate-500">未实现的子页面：${escapeHtml(sub)}</div>`;
}

