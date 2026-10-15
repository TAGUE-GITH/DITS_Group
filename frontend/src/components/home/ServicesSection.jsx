import { getServices } from "../../api/serviceApi";
import useFetch from "../../hooks/useFetch";
import SectionTitle from "../ui/SectionTitle";
import DataState from "../ui/DataState";
import Button from "../ui/Button";
import ServiceCard from "../cards/ServiceCard";

export default function ServicesSection() {
  const { data, loading, error } = useFetch(getServices);
  const services = (data || []).slice(0, 6);

  return (
    <section className="section">
      <div className="container">
        <SectionTitle
          tag="Nos services"
          title="Ce que nous faisons pour vous"
          text="Une offre complète pour répondre à chacun de vos besoins."
        />
        <DataState loading={loading} error={error} empty={!services.length}>
          <div className="grid">
            {services.map((service) => (
              <ServiceCard key={service.id} service={service} />
            ))}
          </div>
          <div className="section-action">
            <Button to="/services" variant="dark">
              Voir tous les services
            </Button>
          </div>
        </DataState>
      </div>
    </section>
  );
}