export function createRouter() {
  const routes = [];

  function compile(pathPattern) {
    // e.g. "/goals/:category" -> regex and keys
    const keys = [];
    const reStr =
      "^" +
      pathPattern
        .replaceAll("/", "\\/")
        .replace(/:([A-Za-z0-9_]+)/g, (_, k) => {
          keys.push(k);
          return "([^\\/]+)";
        }) +
      "$";
    return { re: new RegExp(reStr), keys };
  }

  function add(pathPattern, handler) {
    const { re, keys } = compile(pathPattern);
    routes.push({ re, keys, handler, pathPattern });
  }

  function parseQuery(search) {
    const out = {};
    const sp = new URLSearchParams(search || "");
    for (const [k, v] of sp.entries()) out[k] = v;
    return out;
  }

  async function dispatch() {
    const path = window.location.pathname || "/";
    const query = parseQuery(window.location.search);
    for (const r of routes) {
      const m = path.match(r.re);
      if (!m) continue;
      const params = {};
      r.keys.forEach((k, i) => (params[k] = decodeURIComponent(m[i + 1] || "")));
      return await r.handler({ path, query, params });
    }
    // fallback: redirect to home
    if (path !== "/") {
      navigate("/home/overview");
      return;
    }
  }

  function navigate(to) {
    history.pushState({}, "", to);
    dispatch();
  }

  function start() {
    window.addEventListener("popstate", dispatch);
    document.addEventListener("click", (e) => {
      const a = e.target.closest("a[data-link]");
      if (!a) return;
      const href = a.getAttribute("href");
      if (!href || href.startsWith("http")) return;
      e.preventDefault();
      navigate(href);
    });
    dispatch();
  }

  return { add, start, navigate };
}

