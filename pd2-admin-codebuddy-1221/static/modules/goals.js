import { apiFetch, escapeHtml, pageEnterAnimation, showModal, toast } from "/static/utils.js";

const MODE_LABELS = {
  management: "管理模式",
  weekly: "周报模式",
  personal: "个人模式",
};

function modeTabsHtml(active) {
  return Object.entries(MODE_LABELS)
    .map(([k, label]) => {
      const cls = k === active ? "chip chip-active" : "chip";
      return `<button class="${cls}" data-mode="${k}">${escapeHtml(label)}</button>`;
    })
    .join("");
}

function objectiveCardHtml(node, { collapsed }) {
  const o = node.objective;
  return `
    <div class="card p-4" data-obj="${o.id}">
      <div class="flex items-start justify-between gap-3">
        <div class="min-w-0">
          <div class="flex items-center gap-2">
            <button class="chip h-8 px-3" data-toggle-obj="${o.id}">${collapsed ? "展开" : "收起"}</button>
            <div class="font-semibold text-base break-words">${escapeHtml(o.title)}</div>
          </div>
          <div class="mt-1 text-sm text-slate-600 whitespace-pre-wrap break-words">${escapeHtml(o.description || "")}</div>
          <div class="mt-2 text-xs text-slate-500">
            负责人：${escapeHtml(o.owner || "-")} · 协作：${escapeHtml(o.collaborator || "-")}
            · 优先级：${escapeHtml(o.priority)} · 状态：${escapeHtml(o.status)} · 进度：${escapeHtml(o.progress)}%
          </div>
        </div>
        <div class="flex items-center gap-2 shrink-0">
          <button class="btn btn-secondary h-9 px-3" data-act="obj-edit" data-id="${o.id}">编辑</button>
          <button class="btn btn-secondary h-9 px-3" data-act="obj-del" data-id="${o.id}">删除</button>
        </div>
      </div>
      <div class="${collapsed ? "hidden" : "block"} mt-4 space-y-3" data-obj-body="${o.id}">
        <div class="flex items-center justify-between">
          <div class="text-sm font-medium">关键结果（KR）</div>
          <button class="btn btn-primary h-9 px-3" data-act="kr-new" data-obj="${o.id}">新建 KR</button>
        </div>
        <div class="space-y-3">
          ${node.key_results
            .map((krNode) => {
              const kr = krNode.key_result;
              return `
                <div class="border border-slate-200 rounded-2xl p-3 bg-white" data-kr="${kr.id}">
                  <div class="flex items-start justify-between gap-3">
                    <div class="min-w-0">
                      <div class="font-medium break-words">${escapeHtml(kr.title)}</div>
                      <div class="mt-1 text-sm text-slate-600 whitespace-pre-wrap break-words">${escapeHtml(kr.description || "")}</div>
                      <div class="mt-2 text-xs text-slate-500">
                        负责人：${escapeHtml(kr.owner || "-")} · 协作：${escapeHtml(kr.collaborator || "-")}
                        · 优先级：${escapeHtml(kr.priority)} · 状态：${escapeHtml(kr.status)} · 进度：${escapeHtml(kr.progress)}%
                      </div>
                    </div>
                    <div class="flex items-center gap-2 shrink-0">
                      <button class="btn btn-secondary h-9 px-3" data-act="kr-edit" data-id="${kr.id}" data-obj="${o.id}">编辑</button>
                      <button class="btn btn-secondary h-9 px-3" data-act="kr-del" data-id="${kr.id}" data-obj="${o.id}">删除</button>
                    </div>
                  </div>

                  <div class="mt-3 flex items-center justify-between">
                    <div class="text-sm font-medium">任务项</div>
                    <button class="btn btn-primary h-9 px-3" data-act="task-new" data-kr="${kr.id}">新建任务</button>
                  </div>
                  <div class="mt-2 space-y-2">
                    ${krNode.tasks
                      .map((t) => {
                        return `
                          <div class="border border-slate-200 rounded-xl p-3">
                            <div class="flex items-start justify-between gap-3">
                              <div class="min-w-0">
                                <div class="font-medium break-words">${escapeHtml(t.title)}</div>
                                <div class="mt-1 text-sm text-slate-600 whitespace-pre-wrap break-words">${escapeHtml(t.description || "")}</div>
                                <div class="mt-2 grid grid-cols-2 lg:grid-cols-4 gap-2 text-xs text-slate-500">
                                  <div>主负责人：${escapeHtml(t.owner || "-")}</div>
                                  <div>协作：${escapeHtml(t.collaborator || "-")}</div>
                                  <div>优先级：${escapeHtml(t.priority)}</div>
                                  <div>状态：${escapeHtml(t.status)} / ${escapeHtml(t.progress)}%</div>
                                </div>
                                <div class="mt-2 text-xs text-slate-500 whitespace-pre-wrap break-words">
                                  本周进展：${escapeHtml(t.this_week_progress || "")}
                                </div>
                                ${t.attachments?.length ? `<div class="mt-1 text-xs text-slate-500">附件：${escapeHtml(t.attachments.join(", "))}</div>` : ""}
                              </div>
                              <div class="flex items-center gap-2 shrink-0">
                                <button class="btn btn-secondary h-9 px-3" data-act="task-edit" data-id="${t.id}" data-kr="${kr.id}">编辑</button>
                                <button class="btn btn-secondary h-9 px-3" data-act="task-del" data-id="${t.id}" data-kr="${kr.id}">删除</button>
                              </div>
                            </div>
                          </div>
                        `;
                      })
                      .join("")}
                    ${krNode.tasks.length ? "" : `<div class="text-sm text-slate-400">暂无任务</div>`}
                  </div>
                </div>
              `;
            })
            .join("")}
          ${node.key_results.length ? "" : `<div class="text-sm text-slate-400">暂无 KR</div>`}
        </div>
      </div>
    </div>
  `;
}

function input({ key, label, value = "", type = "text", placeholder = "", full = false, rows = 4, options = [] }) {
  const base = `class="mt-1 w-full rounded-xl border border-slate-200 bg-white px-3 py-2 text-sm outline-none focus:ring-4 focus:ring-blue-200" data-field="${key}"`;
  const head = `<div class="${full ? "sm:col-span-2" : ""}"><div class="text-xs text-slate-500">${escapeHtml(label)}</div>`;
  const tail = `</div>`;
  if (type === "textarea") {
    return `${head}<textarea ${base} rows="${rows}" placeholder="${escapeHtml(placeholder)}">${escapeHtml(value)}</textarea>${tail}`;
  }
  if (type === "select") {
    const opts = options.map((o) => `<option value="${escapeHtml(o)}" ${o === value ? "selected" : ""}>${escapeHtml(o)}</option>`).join("");
    return `${head}<select ${base}>${opts}</select>${tail}`;
  }
  return `${head}<input ${base} type="${type}" value="${escapeHtml(value)}" placeholder="${escapeHtml(placeholder)}"/>${tail}`;
}

function collect(root) {
  const out = {};
  root.querySelectorAll("[data-field]").forEach((el) => {
    const k = el.getAttribute("data-field");
    out[k] = el.value;
  });
  if ("progress" in out) out.progress = Number(out.progress || 0);
  if ("attachments" in out) out.attachments = String(out.attachments || "").split("\n").map((s) => s.trim()).filter(Boolean);
  return out;
}

export async function renderGoals(pageEl, { category, period, mode }) {
  const periods = (await apiFetch("/api/goals/periods")).periods || [];
  period = period || periods[0] || "25-H2";
  mode = mode || "management";

  const headerHtml = `
    <div class="flex flex-col gap-3">
      <div class="flex items-start justify-between gap-4">
        <div>
          <div class="text-2xl font-semibold">目标管理（${escapeHtml(category)}）</div>
          <div class="mt-1 text-sm text-slate-500">OKR：目标(O) → 关键结果(KR) → 任务项</div>
        </div>
        <div class="flex items-center gap-2">
          <button class="btn btn-secondary" id="btnSnapshot">创建快照</button>
          <button class="btn btn-primary" id="btnNewObjective">新建专项</button>
        </div>
      </div>

      <div class="card p-3">
        <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-3">
          <div class="flex items-center gap-2">
            <div class="text-sm text-slate-600">时间周期</div>
            <select id="periodSelect" class="h-10 rounded-xl border border-slate-200 bg-white px-3 text-sm outline-none focus:ring-4 focus:ring-blue-200">
              ${periods.map((p) => `<option value="${escapeHtml(p)}" ${p === period ? "selected" : ""}>${escapeHtml(p)}</option>`).join("")}
            </select>
          </div>

          <div class="flex items-center gap-2" id="modeTabs">
            ${modeTabsHtml(mode)}
          </div>

          <div class="flex items-center gap-2">
            <button class="btn btn-secondary h-10" id="btnExpandAll">一键展开</button>
            <button class="btn btn-secondary h-10" id="btnCollapseAll">一键收起</button>
          </div>
        </div>
      </div>
    </div>
  `;

  pageEl.innerHTML = `${headerHtml}<div class="mt-4" id="goalsBody"></div>`;
  pageEnterAnimation(pageEl);

  const body = pageEl.querySelector("#goalsBody");
  const collapsedObj = new Set();

  async function navTo({ period: p = period, mode: m = mode }) {
    const url = `/goals/${category}?period=${encodeURIComponent(p)}&mode=${encodeURIComponent(m)}`;
    history.pushState({}, "", url);
    await renderGoals(pageEl, { category, period: p, mode: m });
  }

  async function loadTree() {
    const res = await apiFetch(`/api/goals/${category}/tree?period=${encodeURIComponent(period)}`);
    const items = res.items || [];
    body.innerHTML = `
      <div class="space-y-4">
        ${items
          .map((node) => objectiveCardHtml(node, { collapsed: collapsedObj.has(node.objective.id) }))
          .join("")}
        ${items.length ? "" : `<div class="text-sm text-slate-500">暂无数据，点击右上角“新建专项”。</div>`}
      </div>
    `;
  }

  async function loadWeekly({ since }) {
    const res = await apiFetch(`/api/goals/${category}/weekly?period=${encodeURIComponent(period)}&since=${encodeURIComponent(since)}`);
    body.innerHTML = `
      <div class="card p-4">
        <div class="flex items-center justify-between gap-3">
          <div class="font-medium">周报视图</div>
          <div class="flex items-center gap-2">
            <div class="text-sm text-slate-600">日期筛选</div>
            <input id="sinceInput" type="date" value="${escapeHtml(since)}" class="h-10 rounded-xl border border-slate-200 bg-white px-3 text-sm outline-none focus:ring-4 focus:ring-blue-200"/>
            <button class="btn btn-secondary" id="btnSince">应用</button>
          </div>
        </div>
      </div>
      <div class="mt-4 space-y-4">
        ${(res.owners || [])
          .map(
            (g) => `
              <div class="card p-4">
                <div class="font-semibold">${escapeHtml(g.owner)}</div>
                <div class="mt-3 space-y-2">
                  ${g.items
                    .map(
                      (it) => `
                        <div class="border border-slate-200 rounded-xl p-3">
                          <div class="text-sm text-slate-500">O：${escapeHtml(it.objective.title)} · KR：${escapeHtml(it.key_result.title)}</div>
                          <div class="mt-1 font-medium break-words">${escapeHtml(it.task.title)}</div>
                          <div class="mt-1 text-sm text-slate-600 whitespace-pre-wrap break-words">${escapeHtml(it.task.description || "")}</div>
                          <div class="mt-2 text-xs text-slate-500">优先级：${escapeHtml(it.task.priority)} · 状态：${escapeHtml(it.task.status)} · 进度：${escapeHtml(it.task.progress)}%</div>
                          <div class="mt-2 text-xs text-slate-500 whitespace-pre-wrap break-words">本周进展：${escapeHtml(it.task.this_week_progress || "")}</div>
                        </div>
                      `
                    )
                    .join("")}
                </div>
              </div>
            `
          )
          .join("")}
        ${(res.owners || []).length ? "" : `<div class="text-sm text-slate-500">暂无近期更新</div>`}
      </div>
    `;
    body.querySelector("#btnSince")?.addEventListener("click", async () => {
      const v = body.querySelector("#sinceInput")?.value;
      await loadWeekly({ since: v });
    });
  }

  async function loadPersonal({ owner }) {
    body.innerHTML = `
      <div class="card p-4">
        <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-3">
          <div>
            <div class="font-medium">个人视图</div>
            <div class="text-sm text-slate-500">聚焦个人目标追踪，并支持生成绩效评价</div>
          </div>
          <div class="flex items-center gap-2">
            <div class="text-sm text-slate-600">成员</div>
            <input id="ownerInput" class="h-10 rounded-xl border border-slate-200 bg-white px-3 text-sm outline-none focus:ring-4 focus:ring-blue-200" value="${escapeHtml(owner || "")}" placeholder="英文名/姓名"/>
            <button class="btn btn-secondary" id="btnOwner">查询</button>
            <button class="btn btn-primary" id="btnEval">生成绩效评价</button>
          </div>
        </div>
      </div>
      <div class="mt-4" id="personalBody"></div>
    `;
    const personalBody = body.querySelector("#personalBody");

    async function refresh() {
      const who = body.querySelector("#ownerInput")?.value?.trim();
      if (!who) {
        personalBody.innerHTML = `<div class="text-sm text-slate-500">请输入成员名称。</div>`;
        return;
      }
      const res = await apiFetch(
        `/api/goals/${category}/personal?period=${encodeURIComponent(period)}&owner=${encodeURIComponent(who)}`
      );
      personalBody.innerHTML = `
        <div class="space-y-3">
          ${res.items
            .map(
              (it) => `
                <div class="card p-4">
                  <div class="text-sm text-slate-500">O：${escapeHtml(it.objective.title)} · KR：${escapeHtml(it.key_result.title)}</div>
                  <div class="mt-1 font-medium break-words">${escapeHtml(it.task.title)}</div>
                  <div class="mt-1 text-sm text-slate-600 whitespace-pre-wrap break-words">${escapeHtml(it.task.description || "")}</div>
                  <div class="mt-2 text-xs text-slate-500">优先级：${escapeHtml(it.task.priority)} · 状态：${escapeHtml(it.task.status)} · 进度：${escapeHtml(it.task.progress)}%</div>
                  <div class="mt-2 text-xs text-slate-500 whitespace-pre-wrap break-words">本周进展：${escapeHtml(it.task.this_week_progress || "")}</div>
                </div>
              `
            )
            .join("")}
          ${res.items.length ? "" : `<div class="text-sm text-slate-500">暂无数据</div>`}
        </div>
      `;
    }

    body.querySelector("#btnOwner")?.addEventListener("click", refresh);
    body.querySelector("#btnEval")?.addEventListener("click", async () => {
      const who = body.querySelector("#ownerInput")?.value?.trim();
      if (!who) return toast("请先输入成员", { type: "error" });
      try {
        const res = await apiFetch(
          `/api/goals/${category}/evaluation?period=${encodeURIComponent(period)}&owner=${encodeURIComponent(who)}`
        );
        showModal({
          title: "绩效评价（自动生成）",
          contentHtml: `
            <div class="text-slate-700">${escapeHtml(res.summary)}</div>
            <div class="mt-3">
              <div class="text-xs font-semibold text-slate-500">亮点</div>
              <ul class="mt-1 list-disc pl-5 space-y-1">${(res.highlights || []).map((h) => `<li>${escapeHtml(h)}</li>`).join("")}</ul>
            </div>
            <div class="mt-3">
              <div class="text-xs font-semibold text-slate-500">风险</div>
              <ul class="mt-1 list-disc pl-5 space-y-1">${(res.risks || []).map((r) => `<li>${escapeHtml(r)}</li>`).join("")}</ul>
            </div>
          `,
          actions: [{ label: "知道了", primary: true }],
        });
      } catch (e) {
        toast(String(e.message || e), { type: "error", ms: 3500 });
      }
    });

    await refresh();
  }

  function objectiveFormHtml(o = {}) {
    return `
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-3" id="objForm">
        ${input({ key: "title", label: "目标标题", value: o.title || "", full: true })}
        ${input({ key: "priority", label: "优先级", value: o.priority || "P2", type: "select", options: ["P0", "P1", "P2", "P3"] })}
        ${input({ key: "status", label: "状态", value: o.status || "进行中" })}
        ${input({ key: "progress", label: "进度(0-100)", value: String(o.progress ?? 0), type: "number" })}
        ${input({ key: "owner", label: "主负责人", value: o.owner || "" })}
        ${input({ key: "collaborator", label: "协作负责人", value: o.collaborator || "" })}
        ${input({ key: "description", label: "目标描述", value: o.description || "", type: "textarea", full: true, rows: 5 })}
      </div>
    `;
  }

  function krFormHtml(kr = {}) {
    return `
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-3" id="krForm">
        ${input({ key: "title", label: "KR 标题", value: kr.title || "", full: true })}
        ${input({ key: "priority", label: "优先级", value: kr.priority || "P2", type: "select", options: ["P0", "P1", "P2", "P3"] })}
        ${input({ key: "status", label: "状态", value: kr.status || "进行中" })}
        ${input({ key: "progress", label: "进度(0-100)", value: String(kr.progress ?? 0), type: "number" })}
        ${input({ key: "owner", label: "主负责人", value: kr.owner || "" })}
        ${input({ key: "collaborator", label: "协作负责人", value: kr.collaborator || "" })}
        ${input({ key: "description", label: "KR 描述", value: kr.description || "", type: "textarea", full: true, rows: 4 })}
      </div>
    `;
  }

  function taskFormHtml(t = {}) {
    return `
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-3" id="taskForm">
        ${input({ key: "title", label: "任务标题", value: t.title || "", full: true })}
        ${input({ key: "priority", label: "优先级", value: t.priority || "P2", type: "select", options: ["P0", "P1", "P2", "P3"] })}
        ${input({ key: "status", label: "状态", value: t.status || "进行中" })}
        ${input({ key: "progress", label: "进度(0-100)", value: String(t.progress ?? 0), type: "number" })}
        ${input({ key: "owner", label: "主负责人", value: t.owner || "" })}
        ${input({ key: "collaborator", label: "协作负责人", value: t.collaborator || "" })}
        ${input({ key: "description", label: "任务描述", value: t.description || "", type: "textarea", full: true, rows: 4 })}
        ${input({ key: "this_week_progress", label: "本周进展", value: t.this_week_progress || "", type: "textarea", full: true, rows: 3 })}
        ${input({ key: "attachments", label: "附件（每行一个）", value: (t.attachments || []).join("\n"), type: "textarea", full: true, rows: 3 })}
      </div>
    `;
  }

  async function createObjective() {
    showModal({
      title: "新建专项（目标 O）",
      contentHtml: objectiveFormHtml({}),
      actions: [
        { label: "取消" },
        {
          label: "创建",
          primary: true,
          onClick: async () => {
            const data = collect(document.querySelector("#objForm"));
            try {
              await apiFetch(`/api/goals/${category}/objectives`, {
                method: "POST",
                body: { ...data, category, period },
              });
              toast("已创建", { type: "success" });
              await loadTree();
            } catch (e) {
              toast(String(e.message || e), { type: "error", ms: 3500 });
            }
          },
        },
      ],
    });
  }

  async function createSnapshot() {
    try {
      await apiFetch(`/api/goals/${category}/snapshots?period=${encodeURIComponent(period)}`, { method: "POST" });
      toast("快照已创建", { type: "success" });
    } catch (e) {
      toast(String(e.message || e), { type: "error", ms: 3500 });
    }
  }

  pageEl.querySelector("#periodSelect")?.addEventListener("change", async (e) => {
    period = e.target.value;
    await navTo({ period, mode });
  });

  pageEl.querySelector("#modeTabs")?.addEventListener("click", async (e) => {
    const m = e.target?.getAttribute?.("data-mode");
    if (!m) return;
    mode = m;
    await navTo({ period, mode });
  });

  pageEl.querySelector("#btnNewObjective")?.addEventListener("click", createObjective);
  pageEl.querySelector("#btnSnapshot")?.addEventListener("click", createSnapshot);
  pageEl.querySelector("#btnExpandAll")?.addEventListener("click", async () => {
    collapsedObj.clear();
    if (mode === "management") await loadTree();
  });
  pageEl.querySelector("#btnCollapseAll")?.addEventListener("click", async () => {
    // collapse all currently loaded objectives
    document.querySelectorAll("[data-obj]").forEach((el) => collapsedObj.add(Number(el.getAttribute("data-obj"))));
    if (mode === "management") await loadTree();
  });

  // Mode initial render
  if (mode === "management") {
    await loadTree();
  } else if (mode === "weekly") {
    const since = new Date(Date.now() - 14 * 24 * 3600 * 1000).toISOString().slice(0, 10);
    await loadWeekly({ since });
  } else if (mode === "personal") {
    await loadPersonal({ owner: "" });
  } else {
    await loadTree();
  }

  // Management actions
  pageEl.addEventListener("click", async (e) => {
    const toggle = e.target?.getAttribute?.("data-toggle-obj");
    if (toggle) {
      const id = Number(toggle);
      if (collapsedObj.has(id)) collapsedObj.delete(id);
      else collapsedObj.add(id);
      await loadTree();
      return;
    }

    const act = e.target?.getAttribute?.("data-act");
    if (!act) return;
    if (mode !== "management") return;

    const id = Number(e.target.getAttribute("data-id") || 0);
    const objId = Number(e.target.getAttribute("data-obj") || 0);
    const krId = Number(e.target.getAttribute("data-kr") || 0);

    if (act === "obj-edit") {
      const tree = (await apiFetch(`/api/goals/${category}/tree?period=${encodeURIComponent(period)}`)).items || [];
      const node = tree.find((n) => n.objective.id === id);
      if (!node) return;
      showModal({
        title: "编辑目标 O",
        contentHtml: objectiveFormHtml(node.objective),
        actions: [
          { label: "取消" },
          {
            label: "保存",
            primary: true,
            onClick: async () => {
              const data = collect(document.querySelector("#objForm"));
              await apiFetch(`/api/goals/${category}/objectives/${id}`, { method: "PUT", body: data });
              toast("已保存", { type: "success" });
              await loadTree();
            },
          },
        ],
      });
    }

    if (act === "obj-del") {
      showModal({
        title: "确认删除目标？",
        contentHtml: `<div class="text-slate-600">会同时删除其下 KR 与任务项。</div>`,
        actions: [
          { label: "取消" },
          {
            label: "删除",
            primary: true,
            onClick: async () => {
              await apiFetch(`/api/goals/${category}/objectives/${id}`, { method: "DELETE" });
              toast("已删除", { type: "success" });
              await loadTree();
            },
          },
        ],
      });
    }

    if (act === "kr-new") {
      showModal({
        title: "新建 KR",
        contentHtml: krFormHtml({}),
        actions: [
          { label: "取消" },
          {
            label: "创建",
            primary: true,
            onClick: async () => {
              const data = collect(document.querySelector("#krForm"));
              await apiFetch(`/api/goals/${category}/objectives/${objId}/key-results`, { method: "POST", body: data });
              toast("已创建", { type: "success" });
              await loadTree();
            },
          },
        ],
      });
    }

    if (act === "kr-edit") {
      const tree = (await apiFetch(`/api/goals/${category}/tree?period=${encodeURIComponent(period)}`)).items || [];
      const node = tree.find((n) => n.objective.id === objId);
      const krNode = node?.key_results?.find((x) => x.key_result.id === id);
      if (!krNode) return;
      showModal({
        title: "编辑 KR",
        contentHtml: krFormHtml(krNode.key_result),
        actions: [
          { label: "取消" },
          {
            label: "保存",
            primary: true,
            onClick: async () => {
              const data = collect(document.querySelector("#krForm"));
              await apiFetch(`/api/goals/${category}/key-results/${id}`, { method: "PUT", body: data });
              toast("已保存", { type: "success" });
              await loadTree();
            },
          },
        ],
      });
    }

    if (act === "kr-del") {
      showModal({
        title: "确认删除 KR？",
        contentHtml: `<div class="text-slate-600">会同时删除其下任务项。</div>`,
        actions: [
          { label: "取消" },
          {
            label: "删除",
            primary: true,
            onClick: async () => {
              await apiFetch(`/api/goals/${category}/key-results/${id}`, { method: "DELETE" });
              toast("已删除", { type: "success" });
              await loadTree();
            },
          },
        ],
      });
    }

    if (act === "task-new") {
      showModal({
        title: "新建任务项",
        contentHtml: taskFormHtml({}),
        actions: [
          { label: "取消" },
          {
            label: "创建",
            primary: true,
            onClick: async () => {
              const data = collect(document.querySelector("#taskForm"));
              await apiFetch(`/api/goals/${category}/key-results/${krId}/tasks`, { method: "POST", body: data });
              toast("已创建", { type: "success" });
              await loadTree();
            },
          },
        ],
      });
    }

    if (act === "task-edit") {
      const tree = (await apiFetch(`/api/goals/${category}/tree?period=${encodeURIComponent(period)}`)).items || [];
      const node = tree.find((n) => n.key_results?.some((x) => x.key_result.id === krId));
      const krNode = node?.key_results?.find((x) => x.key_result.id === krId);
      const task = krNode?.tasks?.find((x) => x.id === id);
      if (!task) return;
      showModal({
        title: "编辑任务项",
        contentHtml: taskFormHtml(task),
        actions: [
          { label: "取消" },
          {
            label: "保存",
            primary: true,
            onClick: async () => {
              const data = collect(document.querySelector("#taskForm"));
              await apiFetch(`/api/goals/${category}/tasks/${id}`, { method: "PUT", body: data });
              toast("已保存", { type: "success" });
              await loadTree();
            },
          },
        ],
      });
    }

    if (act === "task-del") {
      showModal({
        title: "确认删除任务？",
        contentHtml: `<div class="text-slate-600">该操作不可撤销。</div>`,
        actions: [
          { label: "取消" },
          {
            label: "删除",
            primary: true,
            onClick: async () => {
              await apiFetch(`/api/goals/${category}/tasks/${id}`, { method: "DELETE" });
              toast("已删除", { type: "success" });
              await loadTree();
            },
          },
        ],
      });
    }
  });
}

