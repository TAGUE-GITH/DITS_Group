import { useCallback } from "react";
import { Link, useParams } from "react-router-dom";
import { FiArrowLeft, FiBriefcase, FiCalendar } from "react-icons/fi";
import { getProjectById } from "../api/projectApi";
import { toAssetUrl } from "../api/http";
import useFetch from "../hooks/useFetch";
import { formatDate, splitTags } from "../utils/format";
import PageHeader from "../components/ui/PageHeader";
import DataState from "../components/ui/DataState";
import Button from "../components/ui/Button";
import Tags from "../components/ui/Tags";
import "./Detail.css";
import "./Projects.css";

export default function ProjectDetail() {
  const { id } = useParams();
  const fetcher = useCallback(() => getProjectById(id), [id]);
  const { data: project, loading, error } = useFetch(fetcher);

  return (
    <>
      <PageHeader
        title={project?.title || "Réalisation"}
        parent={{ to: "/realisations", label: "Réalisations" }}
      />
      <section className="section">
        <div className="container">
          <DataState loading={loading} error={error} empty={!project}>
            <div className="detail">
              <article>
                {project?.imageUrl && (
                  <img
                    className="article-cover"
                    src={toAssetUrl(project.imageUrl)}
                    alt={project.title}
                  />
                )}
                <div className="project-meta">
                  {project?.clientName && (
                    <span>
                      <FiBriefcase /> {project.clientName}
                    </span>
                  )}
                  {project?.completionDate && (
                    <span>
                      <FiCalendar /> {formatDate(project.completionDate)}
                    </span>
                  )}
                </div>
                <div className="detail-content">{project?.description}</div>
              </article>

              <aside className="detail-aside">
                {splitTags(project?.technologies).length > 0 && (
                  <div className="card">
                    <h3>Technologies</h3>
                    <Tags value={project?.technologies} />
                  </div>
                )}
                <div className="card">
                  <h3>Un projet similaire ?</h3>
                  <p>Parlons de votre besoin, nous vous répondons rapidement.</p>
                  <Button to="/contact" block>
                    Nous contacter
                  </Button>
                </div>
                <Link to="/realisations" className="back-link">
                  <FiArrowLeft /> Toutes les réalisations
                </Link>
              </aside>
            </div>
          </DataState>
        </div>
      </section>
    </>
  );
}