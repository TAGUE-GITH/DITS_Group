import { useCallback } from "react";
import { Link, useParams } from "react-router-dom";
import { FiArrowLeft, FiUser } from "react-icons/fi";
import { getArticleById } from "../api/articleApi";
import { toAssetUrl } from "../api/http";
import useFetch from "../hooks/useFetch";
import { formatDate } from "../utils/format";
import PageHeader from "../components/ui/PageHeader";
import DataState from "../components/ui/DataState";
import "./Detail.css";

export default function ArticleDetail() {
  const { id } = useParams();
  const fetcher = useCallback(() => getArticleById(id), [id]);
  const { data: article, loading, error } = useFetch(fetcher);

  const date = article?.publicationDate || article?.createdAt;

  return (
    <>
      <PageHeader
        title={article?.title || "Article"}
        parent={{ to: "/articles", label: "Articles" }}
      />
      <section className="section">
        <div className="container article-page">
          <DataState loading={loading} error={error} empty={!article}>
            <article>
              {article?.imageUrl && (
                <img
                  className="article-cover"
                  src={toAssetUrl(article.imageUrl)}
                  alt={article.title}
                />
              )}
              <div className="article-meta-row">
                {date && <span className="badge">{formatDate(date)}</span>}
                {article?.author && (
                  <span className="article-author">
                    <FiUser /> {article.author}
                  </span>
                )}
              </div>
              <p className="article-summary">{article?.summary}</p>
              <div className="detail-content">{article?.content}</div>
            </article>
            <Link to="/articles" className="back-link" style={{ marginTop: "2rem" }}>
              <FiArrowLeft /> Retour aux articles
            </Link>
          </DataState>
        </div>
      </section>
    </>
  );
}