import { useCallback } from "react";
import { Link, useParams } from "react-router-dom";
import { FiArrowLeft, FiLayers } from "react-icons/fi";
import { getServiceById } from "../api/serviceApi";
import { toAssetUrl } from "../api/http";
import useFetch from "../hooks/useFetch";
import PageHeader from "../components/ui/PageHeader";
import DataState from "../components/ui/DataState";
import Button from "../components/ui/Button";
import "./Detail.css";

export default function ServiceDetail() {
  const { id } = useParams();
  const fetcher = useCallback(() => getServiceById(id), [id]);
  const { data: service, loading, error } = useFetch(fetcher);

  const title = service?.title || service?.name || "Service";

  return (
    <>
      <PageHeader
        title={title}
        parent={{ to: "/services", label: "Services" }}
      />
      <section className="section">
        <div className="container">
          <DataState loading={loading} error={error} empty={!service}>
            <div className="detail">
              <article>
                {service?.imageUrl ? (
                  <img
                    className="article-cover"
                    src={toAssetUrl(service.imageUrl)}
                    alt={title}
                  />
                ) : (
                  <span className="detail-icon">
                    <FiLayers />
                  </span>
                )}
                <div className="detail-content">
                  {service?.content || service?.description}
                </div>
              </article>

              <aside className="detail-aside">
                <div className="card">
                  <h3>Ce service vous intéresse ?</h3>
                  <p>Décrivez-nous votre besoin, nous vous répondons rapidement.</p>
                  <Button
                    to={`/contact?service=${encodeURIComponent(title)}`}
                    block
                  >
                    Demander un devis
                  </Button>
                </div>
                <Link to="/services" className="back-link">
                  <FiArrowLeft /> Tous les services
                </Link>
              </aside>
            </div>
          </DataState>
        </div>
      </section>
    </>
  );
}