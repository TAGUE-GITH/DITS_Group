import { Link } from "react-router-dom";
import { FiArrowRight } from "react-icons/fi";
import { toAssetUrl } from "../../api/http";
import { excerpt } from "../../utils/format";
import Tags from "../ui/Tags";
import "./Cards.css";
import "./ProjectCard.css";

export default function ProjectCard({ project }) {
  return (
    <Link
      to={`/realisations/${project.id}`}
      className="card card-hover article-card project-card"
    >
      <div className="article-image">
        {project.imageUrl ? (
          <img src={toAssetUrl(project.imageUrl)} alt={project.title} loading="lazy" />
        ) : (
          <span>DITS</span>
        )}
        {project.clientName && (
          <span className="project-client">{project.clientName}</span>
        )}
      </div>
      <div className="article-body">
        <h3>{project.title}</h3>
        <p>{excerpt(project.description, 110)}</p>
        <Tags value={project.technologies} limit={4} />
        <span className="card-link">
          Voir la réalisation <FiArrowRight />
        </span>
      </div>
    </Link>
  );
}