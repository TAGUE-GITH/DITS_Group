import { request, query } from "./http";

export const getReviews = () => request("/reviews");

export const getReviewsSummary = () => request("/reviews/summary");

export const getMyReview = () => request("/reviews/me", { auth: true });

export const createReview = (data) =>
  request("/reviews", { method: "POST", body: data, auth: true });

export const getAdminReviews = ({ status = "" } = {}) =>
  request(`/admin/reviews${query({ status })}`, { auth: true });

export const updateReviewStatus = (id, status) =>
  request(`/admin/reviews/${id}/status`, {
    method: "PATCH",
    body: { status },
    auth: true,
  });

export const deleteReview = (id) =>
  request(`/admin/reviews/${id}`, { method: "DELETE", auth: true });