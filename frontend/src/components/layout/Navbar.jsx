import { useEffect, useState } from "react";
import { Link, NavLink } from "react-router-dom";
import { navLinks } from "../../data/navigation";
import { useAuth } from "../../context/authContext";
import Button from "../ui/Button";
import "./Navbar.css";

export default function Navbar() {
  const [open, setOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const { isAuthenticated, isAdmin, logout } = useAuth();

  const close = () => setOpen(false);

  const handleLogout = () => {
    logout();
    close();
  };

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener("scroll", onScroll, { passive: true });
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  useEffect(() => {
    document.body.style.overflow = open ? "hidden" : "";
    return () => {
      document.body.style.overflow = "";
    };
  }, [open]);

  return (
    <header
      className={`navbar ${scrolled ? "navbar-scrolled" : ""} ${
        open ? "navbar-open" : ""
      }`}
    >
      <div className="container navbar-inner">
        <Link to="/" className="logo" onClick={close}>
          <span className="logo-mark">D</span>
          <span>
            DITS<em>Group</em>
          </span>
        </Link>

        <nav className={`menu ${open ? "menu-open" : ""}`}>
          <ul>
            {navLinks.map(({ to, label }, index) => (
              <li key={to} style={{ "--i": index }}>
                <NavLink to={to} end={to === "/"} onClick={close}>
                  {label}
                </NavLink>
              </li>
            ))}
          </ul>

          <div className="menu-actions">
            {isAuthenticated ? (
              <>
                <Button
                  to={isAdmin ? "/admin" : "/mon-espace"}
                  variant="outline"
                  onClick={close}
                >
                  {isAdmin ? "Administration" : "Mon espace"}
                </Button>
                <Button onClick={handleLogout}>Déconnexion</Button>
              </>
            ) : (
              <>
                <Button to="/connexion" variant="outline" onClick={close}>
                  Connexion
                </Button>
                <Button to="/inscription" onClick={close}>
                 S'inscrire
                </Button>
              </>
            )}
          </div>
        </nav>

        <button
          className={`burger ${open ? "burger-open" : ""}`}
          onClick={() => setOpen(!open)}
          aria-label={open ? "Fermer le menu" : "Ouvrir le menu"}
          aria-expanded={open}
        >
          <span />
          <span />
          <span />
        </button>
      </div>
    </header>
  );
}