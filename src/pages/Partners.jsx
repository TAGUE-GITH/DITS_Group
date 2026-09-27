import { getPartners } from "../api/partnerApi";
import useFetch from "../hooks/useFetch";
import PageHeader from "../components/ui/PageHeader";
import DataState from "../components/ui/DataState";
import Button from "../components/ui/Button";
import PartnerCard from "../components/cards/PartnerCard";
import usePageTitle from "../hooks/usePageTitle";

export default function Partners() {
  usePageTitle("Nos partenaires", "Des acteurs de confiance avec qui nous construisons des solutions durables.");

  const { data, loading, error } = useFetch(getPartners);

  return (
    <>
      <PageHeader
        title="Nos partenaires"
        text="Des acteurs de confiance avec qui nous construisons des solutions durables."
      />
      <section className="section">
        <div className="container">
          <DataState loading={loading} error={error} empty={!data?.length}>
            <div className="grid">
              {data?.map((partner) => (
                <PartnerCard key={partner.id} partner={partner} />
              ))}
            </div>
          </DataState>
          <div className="section-action">
            <Button to="/contact" variant="dark">
              Devenir partenaire
            </Button>
          </div>
        </div>
      </section>
    </>
  );
}