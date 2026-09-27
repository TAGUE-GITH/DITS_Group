import { Link } from "react-router-dom";
import { FiArrowRight } from "react-icons/fi";
import { toAssetUrl } from "../../api/http";
import { excerpt, formatDate } from "../../utils/format";
import "./Cards.css";

export default function ArticleCard({ article }) {
  const date = article.publicationDate || article.createdAt;

  return (
    <Link to={`/articles/${article.id}`} className="card card-hover article-card">
      <div className="article-image">
        {article.imageUrl ? (
          <img src={toAssetUrl(article.imageUrl)} alt={article.title} loading="lazy" />
        ) : (
          <span>DITS</span>
        )}
      </div>
      <div className="article-body">
        {date && <span className="badge">{formatDate(date)}</span>}
        <h3>{article.title}</h3>
        <p>{excerpt(article.summary, 130)}</p>
        <span className="card-link">
          Lire l'article <FiArrowRight />
        </span>
      </div>
    </Link>
  );
}