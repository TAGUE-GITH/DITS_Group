import { request, query } from "./http";

export const getPartners = () => request("/partners");

export const getPartnerById = (id) => request(`/partners/${id}`);

export const getAdminPartners = ({ search = "", published = "" } = {}) =>
  request(`/admin/partners${query({ search, published })}`, { auth: true });

export const getAdminPartnerById = (id) =>
  request(`/admin/partners/${id}`, { auth: true });

export const getPartnerStats = () =>
  request("/admin/partners/stats", { auth: true });

export const createPartner = (partnerData) =>
  request("/admin/partners", { method: "POST", body: partnerData, auth: true });

export const updatePartner = (id, partnerData) =>
  request(`/admin/partners/${id}`, { method: "PUT", body: partnerData, auth: true });

export const togglePartnerPublished = (id) =>
  request(`/admin/partners/${id}/status`, { method: "PATCH", auth: true });

export const deletePartner = (id) =>
  request(`/admin/partners/${id}`, { method: "DELETE", auth: true });