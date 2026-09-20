const BASE_URL = "http://localhost:8080/api";

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

  if (!response.ok) throw new Error(extractMessage(data, fallback));

  return data;
};

export const download = async (path, fileName = "document") => {
  const response = await fetch(`${BASE_URL}${path}`, {
    headers: { Authorization: `Bearer ${getToken()}` },
  });

  if (!response.ok) {
    const data = await parse(response);
    throw new Error(extractMessage(data, "Impossible de télécharger le fichier."));
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