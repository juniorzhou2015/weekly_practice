import { createRouter } from "/static/router.js";
import { apiFetch, escapeHtml, qs, toast } from "/static/utils.js";
import { renderHome } from "/static/modules/home.js";
import { renderGoals } from "/static/modules/goals.js";
import { renderDuty } from "/static/modules/duty.js";
import { renderExperience } from "/static/modules/experience.js";
import { renderMinutes } from "/static/modules/minutes.js";

const PRIMARY = [
  { key: "home", label: "首页总览", href: "/home/business-boards" },
  { key: "goals", label: "目标管理", href: "/goals/quality" },
  { key: "duty", label: "值班管理", href: "/duty/events" },
  { key: "experience", label: "经验沉淀", href: "/experience/requirement_solution" },
];

const SECONDARY = {
  home: [
    { key: "business-boards", label: "业务板块", href: "/home/business-boards" },
    { key: "team-members", label: "团队成员", href: "/home/team-members" },
    { key: "architecture", label: "架构模块", href: "/home/architecture" },
    { key: "moments", label: "团队剪影", href: "/home/moments" },
  ],
  goals: [
    { key: "quality", label: "质量保障", href: "/goals/quality" },
    { key: "architecture", label: "架构升级", href: "/goals/architecture" },
    { key: "ai", label: "AI实践", href: "/goals/ai" },
    { key: "product", label: "产品需求", href: "/goals/product" },
  ],
  duty: [
    { key: "events", label: "重要事件日历", href: "/duty/events" },
    { key: "schedules", label: "团队值班安排", href: "/duty/schedules" },
  ],
  experience: [
    { key: "requirement_solution", label: "需求方案", href: "/experience/requirement_solution" },
    { key: "incident_history", label: "历史故障", href: "/experience/incident_history" },
    { key: "spring_festival", label: "春节保障", href: "/experience/spring_festival" },
    { key: "probing_alarm", label: "拨测告警", href: "/experience/probing_alarm" },
    { key: "practice_thoughts", label: "实践思考", href: "/experience/practice_thoughts" },
  ],
};

function renderNav({ activePrimaryKey, activeSecondaryKey }) {
  const primaryNav = qs("#primaryNav");
  const secondaryNav = qs("#secondaryNav");

  primaryNav.innerHTML = PRIMARY.map((x) => {
    const cls = x.key === activePrimaryKey ? "chip chip-active" : "chip";
    return `<a class="${cls}" href="${x.href}" data-link>${escapeHtml(x.label)}</a>`;
  }).join("");

  const sec = SECONDARY[activePrimaryKey] || [];
  secondaryNav.innerHTML = sec
    .map((x) => {
      const cls = x.key === activeSecondaryKey ? "chip chip-active" : "chip";
      return `<a class="${cls}" href="${x.href}" data-link>${escapeHtml(x.label)}</a>`;
    })
    .join("");
}

function setRouteHint(text) {
  qs("#routeHint").textContent = text || "";
}

const router = createRouter();

router.add("/home/overview", async () => {
  router.navigate("/home/business-boards");
});

router.add("/home/:sub", async ({ params }) => {
  renderNav({ activePrimaryKey: "home", activeSecondaryKey: params.sub });
  setRouteHint(`/home/${params.sub}`);
  const page = qs("#page");
  await renderHome(page, params.sub);
});

router.add("/goals/:category", async ({ params, query }) => {
  renderNav({ activePrimaryKey: "goals", activeSecondaryKey: params.category });
  const hint = `/goals/${params.category}` + (query.period ? `?period=${query.period}` : "");
  setRouteHint(hint);
  const page = qs("#page");
  await renderGoals(page, { category: params.category, period: query.period, mode: query.mode });
});

router.add("/duty/:sub", async ({ params }) => {
  renderNav({ activePrimaryKey: "duty", activeSecondaryKey: params.sub });
  setRouteHint(`/duty/${params.sub}`);
  const page = qs("#page");
  await renderDuty(page, params.sub);
});

router.add("/experience/:sub", async ({ params }) => {
  renderNav({ activePrimaryKey: "experience", activeSecondaryKey: params.sub });
  setRouteHint(`/experience/${params.sub}`);
  const page = qs("#page");
  await renderExperience(page, params.sub);
});

router.add("/minutes", async () => {
  renderNav({ activePrimaryKey: "home", activeSecondaryKey: "" });
  setRouteHint("/minutes");
  const page = qs("#page");
  await renderMinutes(page);
});

router.add("/", async () => router.navigate("/home/business-boards"));

qs("#btnHealth")?.addEventListener("click", async () => {
  try {
    await apiFetch("/api/health");
    toast("后端健康检查：OK", { type: "success" });
  } catch (e) {
    toast(`健康检查失败：${String(e.message || e)}`, { type: "error", ms: 3500 });
  }
});

router.start();

