import { request } from "./http";

export const getAllUsers = () => request("/admin/users", { auth: true });

export const getPendingUsers = () => request("/admin/users/pending", { auth: true });

export const getActiveUsers = () => request("/admin/users/active", { auth: true });

export const getDisabledUsers = () => request("/admin/users/disabled", { auth: true });

export const activateUser = (id) =>
  request(`/admin/users/${id}/activate`, { method: "PUT", auth: true });

export const disableUser = (id) =>
  request(`/admin/users/${id}/disable`, { method: "PUT", auth: true });

export const deleteUser = (id) =>
  request(`/admin/users/${id}`, { method: "DELETE", auth: true });