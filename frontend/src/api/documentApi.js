import { request, download } from "./http";

export const getMyDocuments = () => request("/documents/me", { auth: true });

export const getMyDocumentById = (id) => request(`/documents/${id}`, { auth: true });

export const downloadMyDocument = (id, fileName) =>
  download(`/documents/${id}/download`, fileName);

export const getAdminDocuments = () => request("/admin/documents", { auth: true });

export const getAdminDocumentById = (id) =>
  request(`/admin/documents/${id}`, { auth: true });

export const uploadDocument = ({ title, description, userId, file }) => {
  const formData = new FormData();

  formData.append("title", title);
  if (description) formData.append("description", description);
  formData.append("userId", userId);
  formData.append("file", file);

  return request("/admin/documents", {
    method: "POST",
    body: formData,
    auth: true,
  });
};

export const updateDocument = (id, data) =>
  request(`/admin/documents/${id}`, { method: "PUT", body: data, auth: true });

export const deleteDocument = (id) =>
  request(`/admin/documents/${id}`, { method: "DELETE", auth: true });

export const downloadAdminDocument = (id, fileName) =>
  download(`/admin/documents/${id}/download`, fileName);