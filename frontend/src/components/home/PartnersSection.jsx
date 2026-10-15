import { getPartners } from "../../api/partnerApi";
import AssetImage from "../ui/AssetImage";
import useFetch from "../../hooks/useFetch";
import SectionTitle from "../ui/SectionTitle";

export default function PartnersSection() {
  const { data, loading, error } = useFetch(getPartners);

  if (loading || error || !data?.length) return null;

  return (
    <section className="section section-alt">
      <div className="container">
        <SectionTitle
          tag="Écosystème"
          title="Nos partenaires"
          text="Des acteurs de confiance avec qui nous construisons des solutions durables."
        />
        <div className="partners-grid">
          {data.map((partner) => {
            const logo = partner.logoUrl || partner.logo;
            const link = partner.websiteUrl || partner.website;

            return (
              <a
                key={partner.id}
                href={link || "#"}
                target={link ? "_blank" : undefined}
                rel={link ? "noreferrer" : undefined}
                className="partner"
                title={partner.name}
              >
                <AssetImage src={logo} alt={partner.name} fallback={partner.name} />
              </a>
            );
          })}
        </div>
      </div>
    </section>
  );
}