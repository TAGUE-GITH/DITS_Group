import { Link } from "react-router-dom";
import { FiArrowRight, FiMapPin } from "react-icons/fi";
import { contractLabel, excerpt, formatDate } from "../../utils/format";
import "./Cards.css";
import "./JobCard.css";

export default function JobCard({ job }) {
  return (
    <Link to={`/carrieres/${job.id}`} className="card card-hover job-card">
      <div className="job-top">
        {job.contractType && (
          <span className="badge">{contractLabel(job.contractType)}</span>
        )}
        {job.createdAt && <small>{formatDate(job.createdAt)}</small>}
      </div>
      <h3>{job.title}</h3>
      {job.location && (
        <p className="job-location">
          <FiMapPin /> {job.location}
        </p>
      )}
      <p>{excerpt(job.description, 120)}</p>
      <span className="card-link">
        Voir l'offre <FiArrowRight />
      </span>
    </Link>
  );
}