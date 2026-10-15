import { FiExternalLink } from "react-icons/fi";
import AssetImage from "../ui/AssetImage";
import { excerpt } from "../../utils/format";
import "./Cards.css";
import "./PartnerCard.css";

export default function PartnerCard({ partner }) {
  const logo = partner.logoUrl || partner.logo;
  const link = partner.websiteUrl || partner.website;

  return (
    <div className="card card-hover partner-card">
      <div className="partner-logo">
        <AssetImage src={logo} alt={partner.name} fallback={<span>{partner.name?.charAt(0)}</span>} />
      </div>
      <h3>{partner.name}</h3>
      <p>{excerpt(partner.description, 120)}</p>
      {link && (
        <a href={link} target="_blank" rel="noreferrer" className="card-link">
          Visiter le site <FiExternalLink />
        </a>
      )}
    </div>
  );
}