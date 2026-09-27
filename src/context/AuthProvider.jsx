import { useState } from "react";
import { AuthContext } from "./authContext";
import * as authApi from "../api/authApi";

const readUser = () => {
  const token = localStorage.getItem("token");
  if (!token || token === "undefined") return null;

  const role = localStorage.getItem("role") || "";

  return {
    token,
    role,
    email: localStorage.getItem("email"),
    isAdmin: role.replace("ROLE_", "") === "ADMIN",
  };
};

const extractRole = (data) => {
  const role = data.role ?? data.roles?.[0] ?? "";
  return typeof role === "object" ? role?.name || "" : String(role);
};

export default function AuthProvider({ children }) {
  const [user, setUser] = useState(readUser);

  const login = async (email, password) => {
    const data = await authApi.login(email, password);
    const token = data?.token || data?.accessToken || data?.jwt;

    if (!token) {
      throw new Error("Réponse de connexion invalide : jeton manquant.");
    }

    localStorage.setItem("token", token);
    localStorage.setItem("email", data.email || email);
    localStorage.setItem("role", extractRole(data));

    const current = readUser();
    setUser(current);
    return current;
  };

  const logout = () => {
    authApi.logout();
    setUser(null);
  };

  const value = {
    user,
    login,
    logout,
    isAuthenticated: Boolean(user),
    isAdmin: Boolean(user?.isAdmin),
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}