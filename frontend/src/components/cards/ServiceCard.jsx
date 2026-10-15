import { Link } from "react-router-dom";
import { FiArrowRight, FiLayers } from "react-icons/fi";
import AssetImage from "../ui/AssetImage";
import { excerpt } from "../../utils/format";
import "./Cards.css";

export default function ServiceCard({ service }) {
  return (
    <Link
      to={`/services/${service.id}`}
      className="card card-hover article-card service-card"
    >
      <div className="article-image">
        <AssetImage src={service.imageUrl} alt={service.title} fallback={<FiLayers />} />
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