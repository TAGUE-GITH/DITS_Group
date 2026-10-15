import { Link } from "react-router-dom";
import { FiCheckCircle } from "react-icons/fi";
import AuthIllustration from "./AuthIllustration";
import "./Auth.css";

const points = [
  "Accès sécurisé à vos documents",
  "Suivi de vos demandes en temps réel",
  "Support réactif de nos équipes",
];

export default function AuthLayout({
  variant,
  visualTitle,
  visualText,
  title,
  subtitle,
  footer,
  children,
}) {
  return (
    <div className="auth">
      <aside className="auth-visual">
        <AuthIllustration variant={variant} />
        <h2>{visualTitle}</h2>
        <p>{visualText}</p>
        <ul className="auth-points">
          {points.map((point) => (
            <li key={point}>
              <FiCheckCircle /> {point}
            </li>
          ))}
        </ul>
      </aside>

      <section className="auth-panel">
        <div className="auth-box">
          <Link to="/" className="auth-logo">
            <span className="auth-logo-mark">D</span>
            <span>
              DITS<em>Group</em>
            </span>
          </Link>

          <h1>{title}</h1>
          <p className="auth-sub">{subtitle}</p>

          {children}

          <p className="auth-footer">{footer}</p>
        </div>
      </section>
    </div>
  );
}