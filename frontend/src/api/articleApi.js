import { request } from "./http";

export const getArticles = () => request("/articles");

export const getArticleById = (id) => request(`/articles/${id}`);

export const getAdminArticles = () => request("/admin/articles", { auth: true });

export const getAdminArticleById = (id) =>
  request(`/admin/articles/${id}`, { auth: true });

export const createArticle = (articleData) =>
  request("/admin/articles", { method: "POST", body: articleData, auth: true });

export const updateArticle = (id, articleData) =>
  request(`/admin/articles/${id}`, { method: "PUT", body: articleData, auth: true });

export const toggleArticleStatus = (id) =>
  request(`/admin/articles/${id}/status`, { method: "PATCH", auth: true });

export const deleteArticle = (id) =>
  request(`/admin/articles/${id}`, { method: "DELETE", auth: true });