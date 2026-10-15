import { request } from "./http";

export const getDashboardStats = () =>
  request("/admin/dashboard/stats", { auth: true });