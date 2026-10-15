import { request, query, download } from "./http";

export const getJobOffers = () => request("/job-offers");

export const getJobOfferById = (id) => request(`/job-offers/${id}`);

export const applyToJobOffer = (jobOfferId, applicationData) => {
  const formData = new FormData();

  ["firstName", "lastName", "email", "phone", "message"].forEach((field) => {
    if (applicationData[field]) formData.append(field, applicationData[field]);
  });

  formData.append("cv", applicationData.cv);

  return request(`/job-offers/${jobOfferId}/applications`, {
    method: "POST",
    body: formData,
  });
};

export const getAdminJobOffers = ({ contractType = "", published = "" } = {}) =>
  request(`/admin/job-offers${query({ contractType, published })}`, { auth: true });

export const getAdminJobOfferById = (id) =>
  request(`/admin/job-offers/${id}`, { auth: true });

export const createJobOffer = (jobOfferData) =>
  request("/admin/job-offers", { method: "POST", body: jobOfferData, auth: true });

export const updateJobOffer = (id, jobOfferData) =>
  request(`/admin/job-offers/${id}`, { method: "PUT", body: jobOfferData, auth: true });

export const toggleJobOfferPublished = (id) =>
  request(`/admin/job-offers/${id}/status`, { method: "PATCH", auth: true });

export const deleteJobOffer = (id) =>
  request(`/admin/job-offers/${id}`, { method: "DELETE", auth: true });

export const getAdminApplications = ({ jobOfferId = "", status = "" } = {}) =>
  request(`/admin/applications${query({ jobOfferId, status })}`, { auth: true });

export const getAdminApplicationById = (id) =>
  request(`/admin/applications/${id}`, { auth: true });

export const updateApplication = (id, applicationData) =>
  request(`/admin/applications/${id}`, {
    method: "PATCH",
    body: applicationData,
    auth: true,
  });

export const deleteApplication = (id) =>
  request(`/admin/applications/${id}`, { method: "DELETE", auth: true });

export const downloadApplicationCv = (id, fileName = "cv") =>
  download(`/admin/applications/${id}/cv`, fileName);