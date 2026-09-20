import { request } from "./http";

export const login = (email, password) =>
  request("/auth/login", {
    method: "POST",
    body: { email, password },
    fallback: "Email ou mot de passe incorrect",
  });

export const register = (userData) =>
  request("/auth/register", {
    method: "POST",
    body: userData,
    fallback: "Impossible de créer le compte.",
  });

export const forgotPassword = (email) =>
  request("/auth/forgot-password", {
    method: "POST",
    body: { email },
    fallback: "Impossible d'envoyer l'email de réinitialisation.",
  });

export const resetPassword = (token, newPassword) =>
  request("/auth/reset-password", {
    method: "POST",
    body: { token, newPassword },
    fallback: "Impossible de réinitialiser le mot de passe.",
  });

export const logout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("email");
  localStorage.removeItem("role");
};