async function api(path, options = {}) {
    const response = await fetch(path, {
        headers: {"Accept": "application/json", ...(options.body ? {"Content-Type": "application/json"} : {})},
        ...options
    });
    const text = await response.text();
    let data;
    try { data = text ? JSON.parse(text) : null; } catch { data = text; }
    if (!response.ok) {
        const message = typeof data === "string" ? data : (data?.message || "Something went wrong.");
        throw new Error(message);
    }
    return data;
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function statusClass(status) {
    const s = String(status || "").toLowerCase();
    if (s.includes("accepted") || s.includes("paid") || s.includes("done")) return "good";
    if (s.includes("rejected")) return "bad";
    return "wait";
}

function toast(message) {
    alert(message);
}
