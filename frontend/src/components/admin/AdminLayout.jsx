import { useState } from "react";
import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";
import { FiExternalLink, FiLogOut, FiMenu, FiX } from "react-icons/fi";
import { adminLinks } from "../../data/adminNav";
import { useAuth } from "../../context/authContext";
import "./AdminLayout.css";

export default function AdminLayout() {
  const [open, setOpen] = useState(false);
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const close = () => setOpen(false);

  const handleLogout = () => {
    logout();
    navigate("/connexion");
  };

  return (
    <div className="admin">
      <div
        className={`admin-overlay ${open ? "admin-overlay-open" : ""}`}
        onClick={close}
      />

      <aside className={`admin-side ${open ? "admin-side-open" : ""}`}>
        <Link to="/admin" className="admin-logo" onClick={close}>
          <span className="admin-logo-mark">D</span>
          <span>
            DITS<em>Admin</em>
          </span>
        </Link>

        <nav className="admin-nav">
          {adminLinks.map(({ to, label, icon: Icon, end }) => (
            <NavLink key={to} to={to} end={end} onClick={close}>
              <Icon /> {label}
            </NavLink>
          ))}
        </nav>

        <div className="admin-side-footer">
          <Link to="/" onClick={close}>
            <FiExternalLink /> Voir le site
          </Link>
          <button onClick={handleLogout}>
            <FiLogOut /> Déconnexion
          </button>
        </div>
      </aside>

      <div className="admin-main">
        <header className="admin-topbar">
          <button
            className="admin-burger"
            onClick={() => setOpen(!open)}
            aria-label={open ? "Fermer le menu" : "Ouvrir le menu"}
          >
            {open ? <FiX /> : <FiMenu />}
          </button>
          <span className="admin-user">{user.email}</span>
        </header>
        <main className="admin-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}