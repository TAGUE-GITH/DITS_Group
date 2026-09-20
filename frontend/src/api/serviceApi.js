import { request } from "./http";

export const getServices = () =>
  request("/services", { fallback: "Impossible de charger les services." });

export const getServiceById = (id) =>
  request(`/services/${id}`, { fallback: "Service introuvable." });

export const getAdminServices = () =>
  request("/admin/services", {
    auth: true,
    fallback: "Impossible de charger les services.",
  });

export const createService = (serviceData) =>
  request("/admin/services", {
    method: "POST",
    body: serviceData,
    auth: true,
    fallback: "Impossible de créer le service.",
  });

export const updateService = (id, serviceData) =>
  request(`/admin/services/${id}`, {
    method: "PUT",
    body: serviceData,
    auth: true,
    fallback: "Impossible de modifier le service.",
  });

export const toggleServiceStatus = (id) =>
  request(`/admin/services/${id}/status`, {
    method: "PATCH",
    auth: true,
    fallback: "Impossible de modifier le statut du service.",
  });

export const deleteService = (id) =>
  request(`/admin/services/${id}`, {
    method: "DELETE",
    auth: true,
    fallback: "Impossible de supprimer le service.",
  });