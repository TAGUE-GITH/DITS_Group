import { Link } from "react-router-dom";
import "./PageHeader.css";

export default function PageHeader({ title, text, parent }) {
  return (
    <section className="page-header">
      <div className="container">
        <nav className="breadcrumb">
          <Link to="/">Accueil</Link>
          <span>/</span>
          {parent && (
            <>
              <Link to={parent.to}>{parent.label}</Link>
              <span>/</span>
            </>
          )}
          <em>{title}</em>
        </nav>
        <h1>{title}</h1>
        {text && <p>{text}</p>}
      </div>
    </section>
  );
}