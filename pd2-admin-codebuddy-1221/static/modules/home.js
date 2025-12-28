import { apiFetch, escapeHtml, pageEnterAnimation, showModal, toast } from "/static/utils.js";
import { renderCrudPage } from "/static/modules/crud.js";

async function uploadFile(file) {
  const fd = new FormData();
  fd.append("file", file);
  const res = await apiFetch("/api/media/upload", { method: "POST", body: fd });
  return res.url;
}

export async function renderHome(pageEl, sub) {
  if (sub === "business-boards") {
    return renderCrudPage(pageEl, {
      title: "业务板块",
      description: "展示团队业务板块：标题与描述（支持增删改查）",
      listPath: "/api/home/business-boards",
      createPath: "/api/home/business-boards",
      updatePath: (id) => `/api/home/business-boards/${id}`,
      deletePath: (id) => `/api/home/business-boards/${id}`,
      columns: [
        { key: "title", label: "板块标题" },
        { key: "description", label: "板块描述", multiline: true },
      ],
      formFields: [
        { key: "title", label: "板块标题", placeholder: "例如：支付与结算" },
        { key: "description", label: "板块描述", type: "textarea", full: true, rows: 5 },
      ],
    });
  }

  if (sub === "team-members") {
    return renderCrudPage(pageEl, {
      title: "团队成员",
      description: "成员信息管理（生日当天自动弹窗提醒）",
      listPath: "/api/home/team-members",
      createPath: "/api/home/team-members",
      updatePath: (id) => `/api/home/team-members/${id}`,
      deletePath: (id) => `/api/home/team-members/${id}`,
      columns: [
        { key: "english_name", label: "英文名" },
        { key: "responsibility", label: "责任田", multiline: true },
        { key: "quality_partner", label: "质量伙伴" },
        { key: "phone", label: "手机号码" },
        { key: "email", label: "电子邮箱" },
        { key: "birthday", label: "生日" },
      ],
      formFields: [
        { key: "english_name", label: "英文名", placeholder: "例如：Alice" },
        { key: "quality_partner", label: "质量伙伴", placeholder: "例如：Bob" },
        { key: "phone", label: "手机号码", placeholder: "例如：13800000000" },
        { key: "email", label: "电子邮箱", placeholder: "例如：a@company.com" },
        { key: "birthday", label: "生日", type: "date" },
        { key: "responsibility", label: "责任田", type: "textarea", full: true, rows: 4 },
      ],
      afterRender: async () => {
        try {
          const birthdays = await apiFetch("/api/home/team-members/birthday-today");
          if (!Array.isArray(birthdays) || birthdays.length === 0) return;
          const names = birthdays.map((m) => escapeHtml(m.english_name)).join("、");
          showModal({
            title: "生日提醒",
            contentHtml: `<div>今天是 <span class="font-semibold">${names}</span> 的生日，祝生日快乐！</div>`,
            actions: [{ label: "庆祝一下", primary: true }],
          });
        } catch (_) {}
      },
    });
  }

  if (sub === "architecture") {
    return renderCrudPage(pageEl, {
      title: "架构模块",
      description: "模块标题 + 简单架构图（支持上传后粘贴 URL）",
      listPath: "/api/home/architecture-modules",
      createPath: "/api/home/architecture-modules",
      updatePath: (id) => `/api/home/architecture-modules/${id}`,
      deletePath: (id) => `/api/home/architecture-modules/${id}`,
      columns: [
        { key: "title", label: "模块标题" },
        { key: "diagram_url", label: "架构图URL", multiline: true },
      ],
      formFields: [
        { key: "title", label: "模块标题" },
        { key: "diagram_url", label: "架构图URL", placeholder: "例如：/uploads/xxx.png" },
        { key: "diagram_file", label: "上传架构图（可选）", type: "file", accept: "image/*", full: true },
      ],
      mapCreatePayload: async (data) => {
        if (data.diagram_file instanceof File && data.diagram_file.size) {
          data.diagram_url = await uploadFile(data.diagram_file);
        }
        delete data.diagram_file;
        return data;
      },
      mapUpdatePayload: async (data) => {
        if (data.diagram_file instanceof File && data.diagram_file.size) {
          data.diagram_url = await uploadFile(data.diagram_file);
        }
        delete data.diagram_file;
        return data;
      },
    });
  }

  if (sub === "moments") {
    pageEl.innerHTML = `
      <div class="flex items-start justify-between gap-4">
        <div>
          <div class="text-2xl font-semibold">团队剪影</div>
          <div class="mt-1 text-sm text-slate-500">支持图片/视频轮播展示与上传/删除</div>
        </div>
        <div class="flex items-center gap-2">
          <label class="btn btn-primary cursor-pointer">
            上传媒体
            <input id="momentFile" type="file" class="hidden" accept="image/*,video/*"/>
          </label>
          <button class="btn btn-secondary" id="btnRefresh">刷新</button>
        </div>
      </div>

      <div class="mt-5 grid grid-cols-1 lg:grid-cols-3 gap-4">
        <div class="lg:col-span-2 card p-4">
          <div class="font-medium">轮播预览</div>
          <div id="carousel" class="mt-3 rounded-2xl overflow-hidden bg-slate-100 aspect-video flex items-center justify-center text-slate-400">
            暂无媒体
          </div>
        </div>
        <div class="card p-4">
          <div class="font-medium">媒体列表</div>
          <div id="momentList" class="mt-3 space-y-2"></div>
        </div>
      </div>
    `;
    pageEnterAnimation(pageEl);

    async function load() {
      const items = await apiFetch("/api/home/team-moments");
      const list = Array.isArray(items) ? items : [];
      const listEl = document.querySelector("#momentList");
      const carouselEl = document.querySelector("#carousel");
      if (!list.length) {
        listEl.innerHTML = `<div class="text-sm text-slate-500">暂无数据</div>`;
        carouselEl.innerHTML = `暂无媒体`;
        return;
      }
      listEl.innerHTML = list
        .map(
          (m) => `
          <div class="flex items-center justify-between gap-2 border border-slate-200 rounded-xl p-2 bg-white">
            <div class="min-w-0">
              <div class="text-sm font-medium truncate">${escapeHtml(m.title || "(未命名)")}</div>
              <div class="text-xs text-slate-500 truncate">${escapeHtml(m.media_type)} · ${escapeHtml(m.url)}</div>
            </div>
            <button class="btn btn-secondary h-9 px-3" data-del="${m.id}">删除</button>
          </div>
        `
        )
        .join("");

      let idx = 0;
      const renderSlide = () => {
        const m = list[idx % list.length];
        if (m.media_type === "video") {
          carouselEl.innerHTML = `<video class="w-full h-full object-contain bg-black" src="${escapeHtml(m.url)}" controls></video>`;
        } else {
          carouselEl.innerHTML = `<img class="w-full h-full object-contain" src="${escapeHtml(m.url)}" alt="moment"/>`;
        }
      };
      renderSlide();
      // simple autoplay
      clearInterval(window.__pd2CarouselTimer);
      window.__pd2CarouselTimer = setInterval(() => {
        idx += 1;
        renderSlide();
      }, 3500);
    }

    document.querySelector("#btnRefresh")?.addEventListener("click", async () => {
      try {
        await load();
        toast("已刷新", { type: "success" });
      } catch (e) {
        toast(String(e.message || e), { type: "error", ms: 3500 });
      }
    });

    document.querySelector("#momentList")?.addEventListener("click", async (e) => {
      const id = e.target?.getAttribute?.("data-del");
      if (!id) return;
      showModal({
        title: "确认删除媒体？",
        contentHtml: `<div class="text-slate-600">仅删除数据库记录；如需清理文件可在上传管理中删除。</div>`,
        actions: [
          { label: "取消" },
          {
            label: "删除",
            primary: true,
            onClick: async () => {
              await apiFetch(`/api/home/team-moments/${id}`, { method: "DELETE" });
              toast("已删除", { type: "success" });
              await load();
            },
          },
        ],
      });
    });

    document.querySelector("#momentFile")?.addEventListener("change", async (e) => {
      const file = e.target.files?.[0];
      if (!file) return;
      try {
        const url = await uploadFile(file);
        const media_type = file.type.startsWith("video/") ? "video" : "image";
        await apiFetch("/api/home/team-moments", {
          method: "POST",
          body: { media_type, title: file.name, url },
        });
        toast("上传成功", { type: "success" });
        e.target.value = "";
        await load();
      } catch (err) {
        toast(String(err.message || err), { type: "error", ms: 3500 });
      }
    });

    await load();
    return;
  }

  pageEl.innerHTML = `<div class="text-slate-500">未实现的子页面：${escapeHtml(sub)}</div>`;
}

