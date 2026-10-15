import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../../context/authContext";

export default function ProtectedRoute({ admin = false }) {
  const { isAuthenticated, isAdmin } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    return <Navigate to="/connexion" state={{ from: location.pathname }} replace />;
  }

  if (admin && !isAdmin) {
    return <Navigate to="/mon-espace" replace />;
  }

  return <Outlet />;
}