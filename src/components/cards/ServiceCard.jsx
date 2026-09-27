import { Link } from "react-router-dom";
import { FiArrowRight, FiLayers } from "react-icons/fi";
import { toAssetUrl } from "../../api/http";
import { excerpt } from "../../utils/format";
import "./Cards.css";

export default function ServiceCard({ service }) {
  return (
    <Link
      to={`/services/${service.id}`}
      className="card card-hover article-card service-card"
    >
      <div className="article-image">
        {service.imageUrl ? (
          <img src={toAssetUrl(service.imageUrl)} alt={service.title} loading="lazy" />
        ) : (
          <FiLayers />
        )}
      </div>
      <div className="article-body">
        <h3>{service.title || service.name}</h3>
        <p>{excerpt(service.description, 110)}</p>
        <span className="card-link">
          En savoir plus <FiArrowRight />
        </span>
      </div>
    </Link>
  );
}