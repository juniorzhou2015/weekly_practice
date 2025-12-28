import { apiFetch, escapeHtml, pageEnterAnimation, qs, showModal, toast } from "/static/utils.js";

function inputHtml(field, value) {
  const v = value ?? "";
  const common = `class="mt-1 w-full rounded-xl border border-slate-200 bg-white px-3 py-2 text-sm outline-none focus:ring-4 focus:ring-blue-200"`;
  if (field.type === "file") {
    const accept = field.accept ? ` accept="${escapeHtml(field.accept)}"` : "";
    return `<input ${common} type="file"${accept} data-field="${field.key}"/>`;
  }
  if (field.type === "textarea") {
    return `<textarea ${common} rows="${field.rows || 4}" data-field="${field.key}">${escapeHtml(v)}</textarea>`;
  }
  if (field.type === "date") {
    return `<input ${common} type="date" data-field="${field.key}" value="${escapeHtml(v ? String(v).slice(0, 10) : "")}"/>`;
  }
  if (field.type === "select") {
    const opts = (field.options || []).map((o) => `<option value="${escapeHtml(o)}" ${o === v ? "selected" : ""}>${escapeHtml(o)}</option>`).join("");
    return `<select ${common} data-field="${field.key}">${opts}</select>`;
  }
  return `<input ${common} type="text" data-field="${field.key}" value="${escapeHtml(v)}" placeholder="${escapeHtml(field.placeholder || "")}"/>`;
}

function collectForm(root, fields) {
  const out = {};
  for (const f of fields) {
    const el = root.querySelector(`[data-field="${f.key}"]`);
    if (!el) continue;
    let v = el.value;
    if (f.type === "file") v = el.files?.[0] || null;
    if (f.type === "number") v = Number(v || 0);
    out[f.key] = v;
  }
  return out;
}

export async function renderCrudPage(pageEl, cfg) {
  const {
    title,
    description = "",
    listPath,
    createPath,
    updatePath, // (id) => path
    deletePath, // (id) => path
    columns = [],
    formFields = [],
    afterRender,
    mapCreatePayload,
    mapUpdatePayload,
  } = cfg;

  pageEl.innerHTML = `
    <div class="flex items-start justify-between gap-4">
      <div>
        <div class="text-2xl font-semibold">${escapeHtml(title)}</div>
        ${description ? `<div class="mt-1 text-sm text-slate-500">${escapeHtml(description)}</div>` : ""}
      </div>
      <div class="flex items-center gap-2">
        <button class="btn btn-secondary" id="btnRefresh">刷新</button>
        <button class="btn btn-primary" id="btnNew">新建</button>
      </div>
    </div>

    <div class="mt-5 card overflow-hidden">
      <div class="overflow-auto">
        <table class="min-w-full text-sm">
          <thead class="bg-slate-50 text-slate-600">
            <tr>
              ${columns.map((c) => `<th class="text-left font-medium px-4 py-3 whitespace-nowrap">${escapeHtml(c.label)}</th>`).join("")}
              <th class="text-right font-medium px-4 py-3 whitespace-nowrap">操作</th>
            </tr>
          </thead>
          <tbody id="tbody" class="divide-y divide-slate-100"></tbody>
        </table>
      </div>
    </div>
  `;
  pageEnterAnimation(pageEl);

  async function load() {
    const items = await apiFetch(listPath);
    const arr = Array.isArray(items) ? items : items.items || items.data || [];
    const tbody = qs("#tbody", pageEl);
    tbody.innerHTML = arr
      .map((it) => {
        const tds = columns
          .map((c) => {
            const raw = c.render ? c.render(it) : it[c.key];
            const val = raw === undefined || raw === null ? "" : String(raw);
            const cls = c.multiline ? "whitespace-pre-wrap break-words" : "whitespace-nowrap";
            return `<td class="px-4 py-3 align-top ${cls}">${escapeHtml(val)}</td>`;
          })
          .join("");
        return `
          <tr data-id="${it.id}">
            ${tds}
            <td class="px-4 py-3 align-top">
              <div class="flex items-center justify-end gap-2">
                <button class="btn btn-secondary h-9 px-3" data-act="edit">编辑</button>
                <button class="btn btn-secondary h-9 px-3" data-act="del">删除</button>
              </div>
            </td>
          </tr>
        `;
      })
      .join("");
  }

  async function openForm({ mode, item }) {
    const fields = formFields.length ? formFields : columns;
    const contentHtml = `
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-3" id="crudForm">
        ${fields
          .map(
            (f) => `
            <div class="${f.full ? "sm:col-span-2" : ""}">
              <div class="text-xs text-slate-500">${escapeHtml(f.label || f.key)}</div>
              ${inputHtml(f, item ? item[f.key] : f.defaultValue)}
            </div>
          `
          )
          .join("")}
      </div>
    `;
    showModal({
      title: mode === "create" ? `新建：${title}` : `编辑：${title}`,
      contentHtml,
      actions: [
        { label: "取消" },
        {
          label: "保存",
          primary: true,
          closeOnClick: false,
          onClick: async ({ close }) => {
            try {
              const form = document.querySelector("#crudForm");
              const data = collectForm(form, fields);
              if (mode === "create") {
                const payload = mapCreatePayload ? await mapCreatePayload(data) : data;
                await apiFetch(createPath, { method: "POST", body: payload });
                toast("已创建", { type: "success" });
              } else {
                const payload = mapUpdatePayload ? await mapUpdatePayload(data) : data;
                await apiFetch(updatePath(item.id), { method: "PUT", body: payload });
                toast("已保存", { type: "success" });
              }
              close();
              await load();
              if (afterRender) await afterRender({ pageEl });
            } catch (e) {
              toast(String(e.message || e), { type: "error", ms: 3500 });
            }
          },
        },
      ],
    });
  }

  pageEl.addEventListener("click", async (e) => {
    const act = e.target?.getAttribute?.("data-act");
    if (!act) return;
    const tr = e.target.closest("tr[data-id]");
    const id = Number(tr?.getAttribute("data-id"));
    if (!id) return;
    if (act === "edit") {
      const items = await apiFetch(listPath);
      const arr = Array.isArray(items) ? items : items.items || items.data || [];
      const item = arr.find((x) => x.id === id);
      await openForm({ mode: "update", item });
    }
    if (act === "del") {
      showModal({
        title: "确认删除？",
        contentHtml: `<div class="text-slate-600">该操作不可撤销。</div>`,
        actions: [
          { label: "取消" },
          {
            label: "删除",
            primary: true,
            onClick: async () => {
              try {
                await apiFetch(deletePath(id), { method: "DELETE" });
                toast("已删除", { type: "success" });
                await load();
                if (afterRender) await afterRender({ pageEl });
              } catch (e) {
                toast(String(e.message || e), { type: "error", ms: 3500 });
              }
            },
          },
        ],
      });
    }
  });

  qs("#btnNew", pageEl)?.addEventListener("click", () => openForm({ mode: "create", item: null }));
  qs("#btnRefresh", pageEl)?.addEventListener("click", async () => {
    try {
      await load();
      if (afterRender) await afterRender({ pageEl });
      toast("已刷新", { type: "success" });
    } catch (e) {
      toast(String(e.message || e), { type: "error", ms: 3500 });
    }
  });

  await load();
  if (afterRender) await afterRender({ pageEl });
}

