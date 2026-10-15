import { Link } from "react-router-dom";
import { FaFacebookF, FaLinkedinIn, FaTwitter } from "react-icons/fa";
import { FiMail, FiMapPin, FiPhone } from "react-icons/fi";
import { navLinks } from "../../data/navigation";
import { company } from "../../data/company";
import NewsletterForm from "./NewsletterForm";
import "./Footer.css";

const accountLinks = [
  { to: "/connexion", label: "Connexion" },
  { to: "/inscription", label: "Créer un compte" },
  { to: "/mon-espace", label: "Mon espace client" },
  { to: "/mot-de-passe-oublie", label: "Mot de passe oublié" },
];

const socials = [
  { icon: FaLinkedinIn, href: "#", label: "LinkedIn" },
  { icon: FaFacebookF, href: "#", label: "Facebook" },
  { icon: FaTwitter, href: "#", label: "Twitter" },
];

export default function Footer() {
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-newsletter">
          <div>
            <h3>Restez informé</h3>
            <p>Recevez nos actualités et conseils directement par email.</p>
          </div>
          <NewsletterForm />
        </div>
      </div>

      <div className="container footer-grid">
        <div className="footer-brand">
          <Link to="/" className="footer-logo">
            DITS<em>Group</em>
          </Link>
          <p>
            Conseil et expertise en systèmes d'information : conception et
            administration de bases de données, sur le cloud et en on-premise.
          </p>
          <div className="footer-socials">
            {socials.map(({ icon: Icon, href, label }) => (
              <a key={label} href={href} aria-label={label}>
                <Icon />
              </a>
            ))}
          </div>
        </div>

        <div>
          <h4>Navigation</h4>
          <ul>
            {navLinks.map(({ to, label }) => (
              <li key={to}>
                <Link to={to}>{label}</Link>
              </li>
            ))}
          </ul>
        </div>

        <div>
          <h4>Mon compte</h4>
          <ul>
            {accountLinks.map(({ to, label }) => (
              <li key={to}>
                <Link to={to}>{label}</Link>
              </li>
            ))}
          </ul>
        </div>

        <div>
          <h4>Contact</h4>
          <ul className="footer-contact">
            <li>
              <FiMail /> {company.email}
            </li>
            <li>
              <FiPhone /> {company.phone}
            </li>
            <li>
              <FiMapPin /> {company.address}
            </li>
          </ul>
        </div>
      </div>

      <div className="footer-bottom">
        <div className="container">
          <p>
            © {new Date().getFullYear()} {company.name}. Tous droits réservés.
          </p>
        </div>
      </div>
    </footer>
  );
}