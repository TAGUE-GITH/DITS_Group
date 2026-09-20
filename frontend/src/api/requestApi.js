import { request, query } from "./http";

export const createRequest = (requestData) =>
  request("/requests", { method: "POST", body: requestData });

export const getAdminRequests = ({ type = "", status = "" } = {}) =>
  request(`/admin/requests${query({ type, status })}`, { auth: true });

export const getAdminRequestById = (id) =>
  request(`/admin/requests/${id}`, { auth: true });

export const updateAdminRequest = (id, data) =>
  request(`/admin/requests/${id}`, { method: "PATCH", body: data, auth: true });

export const deleteAdminRequest = (id) =>
  request(`/admin/requests/${id}`, { method: "DELETE", auth: true });