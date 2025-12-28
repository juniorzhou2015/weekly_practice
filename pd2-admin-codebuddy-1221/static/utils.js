export function qs(sel, root = document) {
  return root.querySelector(sel);
}

export function qsa(sel, root = document) {
  return Array.from(root.querySelectorAll(sel));
}

export function escapeHtml(str) {
  return String(str ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}

export async function apiFetch(path, { method = "GET", body, headers } = {}) {
  const init = { method, headers: { ...(headers || {}) } };
  if (body !== undefined) {
    if (body instanceof FormData) {
      init.body = body;
    } else {
      init.headers["Content-Type"] = "application/json";
      init.body = JSON.stringify(body);
    }
  }
  const res = await fetch(path, init);
  if (!res.ok) {
    let detail = `${res.status} ${res.statusText}`;
    try {
      const j = await res.json();
      detail = j.detail || JSON.stringify(j);
    } catch (_) {}
    throw new Error(detail);
  }
  const ct = res.headers.get("content-type") || "";
  if (ct.includes("application/json")) return await res.json();
  return await res.text();
}

export function toast(msg, { type = "info", ms = 2000 } = {}) {
  const root = qs("#toastRoot");
  const el = document.createElement("div");
  const colors =
    type === "error"
      ? "bg-red-600"
      : type === "success"
        ? "bg-emerald-600"
        : "bg-slate-900";
  el.className = `${colors} text-white text-sm px-4 py-2 rounded-xl shadow-lg`;
  el.textContent = msg;
  root.appendChild(el);
  setTimeout(() => el.remove(), ms);
}

export function showModal({ title = "提示", contentHtml = "", actions = [] }) {
  const root = qs("#modalRoot");
  root.innerHTML = `
    <div class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 modal-backdrop"></div>
      <div class="relative w-full max-w-lg card p-5">
        <div class="flex items-start justify-between gap-3">
          <div>
            <div class="text-lg font-semibold">${escapeHtml(title)}</div>
          </div>
          <button class="btn btn-secondary h-9 px-3" data-modal-close>关闭</button>
        </div>
        <div class="mt-3 text-sm text-slate-700 space-y-2">${contentHtml}</div>
        <div class="mt-5 flex items-center justify-end gap-2" id="modalActions"></div>
      </div>
    </div>
  `;
  const close = () => (root.innerHTML = "");
  qs("[data-modal-close]", root)?.addEventListener("click", close);
  qs(".modal-backdrop", root)?.addEventListener("click", close);

  const actionRoot = qs("#modalActions", root);
  actions.forEach((a) => {
    const b = document.createElement("button");
    b.className = `btn ${a.primary ? "btn-primary" : "btn-secondary"} h-9 px-3`;
    b.textContent = a.label;
    b.addEventListener("click", async () => {
      if (a.onClick) await a.onClick({ close });
      if (a.closeOnClick !== false) close();
    });
    actionRoot.appendChild(b);
  });

  return { close };
}

export function pageEnterAnimation(el) {
  el.classList.add("enter");
  requestAnimationFrame(() => {
    el.classList.add("enter-active");
    el.classList.remove("enter");
    setTimeout(() => el.classList.remove("enter-active"), 220);
  });
}

