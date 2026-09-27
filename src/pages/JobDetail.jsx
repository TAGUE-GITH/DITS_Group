import { useCallback } from "react";
import { Link, useParams } from "react-router-dom";
import {
  FiArrowLeft,
  FiBriefcase,
  FiCalendar,
  FiClock,
  FiMapPin,
} from "react-icons/fi";
import { getJobOfferById } from "../api/jobApi";
import useFetch from "../hooks/useFetch";
import { contractLabel, formatDate } from "../utils/format";
import PageHeader from "../components/ui/PageHeader";
import DataState from "../components/ui/DataState";
import Alert from "../components/ui/Alert";
import ApplicationForm from "../components/careers/ApplicationForm";
import "./Detail.css";
import "./Careers.css";
import usePageTitle from "../hooks/usePageTitle";

export default function JobDetail() {
  const { id } = useParams();
  const fetcher = useCallback(() => getJobOfferById(id), [id]);
  const { data: job, loading, error } = useFetch(fetcher);

  usePageTitle(job?.title, job?.description);

  const closingDate = job?.closingDate;
  const closed =
    Boolean(closingDate) && new Date(`${closingDate}T23:59:59`) < new Date();

  return (
    <>
      <PageHeader
        title={job?.title || "Offre d'emploi"}
        parent={{ to: "/carrieres", label: "Carrières" }}
      />
      <section className="section">
        <div className="container">
          <DataState loading={loading} error={error} empty={!job}>
            <div className="detail">
              <article>
                <div className="job-meta">
                  {job?.contractType && (
                    <span>
                      <FiBriefcase /> {contractLabel(job.contractType)}
                    </span>
                  )}
                  {job?.location && (
                    <span>
                      <FiMapPin /> {job.location}
                    </span>
                  )}
                  {job?.createdAt && (
                    <span>
                      <FiCalendar /> Publiée le {formatDate(job.createdAt)}
                    </span>
                  )}
                  {closingDate && (
                    <span>
                      <FiClock /> Jusqu'au {formatDate(closingDate)}
                    </span>
                  )}
                </div>

                <div className="detail-content">{job?.description}</div>

                {job?.requirements && (
                  <>
                    <h3 className="detail-subtitle">Profil et compétences</h3>
                    <div className="detail-content">{job.requirements}</div>
                  </>
                )}

                <Link to="/carrieres" className="back-link" style={{ marginTop: "2rem" }}>
                  <FiArrowLeft /> Toutes les offres
                </Link>
              </article>

              <aside className="card apply">
                <h3>Postuler à cette offre</h3>
                {closed ? (
                  <Alert>Les candidatures pour cette offre sont closes.</Alert>
                ) : (
                  <>
                    <p>Renseignez vos informations et joignez votre CV.</p>
                    <ApplicationForm jobOfferId={id} />
                  </>
                )}
              </aside>
            </div>
          </DataState>
        </div>
      </section>
    </>
  );
}