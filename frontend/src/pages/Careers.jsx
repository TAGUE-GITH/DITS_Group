import { useMemo, useState } from "react";
import { getJobOffers } from "../api/jobApi";
import useFetch from "../hooks/useFetch";
import { contractLabel } from "../utils/format";
import PageHeader from "../components/ui/PageHeader";
import DataState from "../components/ui/DataState";
import JobCard from "../components/cards/JobCard";
import "./Careers.css";
import usePageTitle from "../hooks/usePageTitle";

export default function Careers() {
  usePageTitle("Carrières", "Rejoignez notre équipe et participez à des projets innovants.");

  const [type, setType] = useState("");
  const { data, loading, error } = useFetch(getJobOffers);

  const types = useMemo(
    () => [...new Set((data || []).map((offer) => offer.contractType).filter(Boolean))],
    [data]
  );

  const offers = useMemo(
    () => (data || []).filter((offer) => !type || offer.contractType === type),
    [data, type]
  );

  return (
    <>
      <PageHeader
        title="Rejoignez DITS Group"
        text="Découvrez nos offres et participez à des projets d'envergure autour des bases de données et du cloud."
      />
      <section className="section">
        <div className="container">
          {types.length > 0 && (
            <div className="chips">
              {["", ...types].map((item) => (
                <button
                  key={item || "all"}
                  className={`chip ${type === item ? "chip-active" : ""}`}
                  onClick={() => setType(item)}
                >
                  {item ? contractLabel(item) : "Toutes les offres"}
                </button>
              ))}
            </div>
          )}

          <DataState
            loading={loading}
            error={error}
            empty={!offers.length}
            emptyText="Aucune offre disponible pour le moment. Revenez bientôt !"
          >
            <div className="grid">
              {offers.map((job) => (
                <JobCard key={job.id} job={job} />
              ))}
            </div>
          </DataState>
        </div>
      </section>
    </>
  );
}