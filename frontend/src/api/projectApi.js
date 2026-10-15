import { request } from "./http";

export const getProjects = () => request("/projects");

export const getProjectById = (id) => request(`/projects/${id}`);

export const getAdminProjects = () => request("/admin/projects", { auth: true });

export const getAdminProjectById = (id) =>
  request(`/admin/projects/${id}`, { auth: true });

export const createProject = (projectData) =>
  request("/admin/projects", { method: "POST", body: projectData, auth: true });

export const updateProject = (id, projectData) =>
  request(`/admin/projects/${id}`, { method: "PUT", body: projectData, auth: true });

export const toggleProjectPublished = (id) =>
  request(`/admin/projects/${id}/status`, { method: "PATCH", auth: true });

export const deleteProject = (id) =>
  request(`/admin/projects/${id}`, { method: "DELETE", auth: true });