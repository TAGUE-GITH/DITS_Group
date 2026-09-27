const BASE_URL = "http://localhost:8080/api";
export const ASSET_BASE_URL = BASE_URL.replace(/\/api\/?$/, "");

export const toAssetUrl = (path) => {
  if (!path) return "";
  if (/^https?:\/\//.test(path)) return path;
  return `${ASSET_BASE_URL}${path}`;
};

const authMessages = {
  401: "Session expirée ou invalide. Déconnectez-vous puis reconnectez-vous.",
  403: "Accès refusé : ce compte n'a pas les droits nécessaires.",
};

const getToken = () => localStorage.getItem("token");

const parse = async (response) => {
  const text = await response.text();
  if (!text) return null;
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
};

const extractMessage = (data, fallback) => {
  if (typeof data === "string" && data) return data;
  if (data && typeof data === "object") {
    if (data.message) return data.message;
    if (data.error) return data.error;
    const values = Object.values(data);
    if (values.length) return values.join(" ");
  }
  return fallback;
};

const createError = (data, status, fallback, auth = false) => {
  const defaultMessage = (auth && authMessages[status]) || fallback;
  const error = new Error(extractMessage(data, defaultMessage));
  error.status = status;
  return error;
};

export const query = (params = {}) => {
  const search = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== "" && value !== null && value !== undefined) {
      search.append(key, value);
    }
  });
  const result = search.toString();
  return result ? `?${result}` : "";
};

export const request = async (
  path,
  { method = "GET", body, auth = false, fallback = "Une erreur est survenue." } = {}
) => {
  const headers = {};
  const isForm = body instanceof FormData;

  if (body && !isForm) headers["Content-Type"] = "application/json";
  if (auth) headers.Authorization = `Bearer ${getToken()}`;

  const response = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body ? (isForm ? body : JSON.stringify(body)) : undefined,
  });

  const data = await parse(response);

  if (!response.ok) throw createError(data, response.status, fallback, auth);

  return data;
};

export const download = async (path, fileName = "document") => {
  const response = await fetch(`${BASE_URL}${path}`, {
    headers: { Authorization: `Bearer ${getToken()}` },
  });

  if (!response.ok) {
    const data = await parse(response);
    throw createError(data, response.status, "Impossible de télécharger le fichier.", true);
  }

  const blob = await response.blob();
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");

  link.href = url;
  link.download = fileName;
  document.body.appendChild(link);
  link.click();
  link.remove();
  URL.revokeObjectURL(url);
};