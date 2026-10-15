import { request, query } from "./http";

export const subscribeNewsletter = (email) =>
  request("/newsletter/subscribe", { method: "POST", body: { email } });

export const unsubscribeNewsletter = (email) =>
  request("/newsletter/unsubscribe", { method: "POST", body: { email } });

export const getAdminSubscribers = ({ search = "", status = "" } = {}) =>
  request(`/admin/newsletter${query({ search, status })}`, { auth: true });

export const getAdminSubscriberById = (id) =>
  request(`/admin/newsletter/${id}`, { auth: true });

export const updateSubscriberStatus = (id, status) =>
  request(`/admin/newsletter/${id}/status`, {
    method: "PATCH",
    body: { status },
    auth: true,
  });

export const deleteSubscriber = (id) =>
  request(`/admin/newsletter/${id}`, { method: "DELETE", auth: true });

export const getNewsletterStats = () =>
  request("/admin/newsletter/stats", { auth: true });