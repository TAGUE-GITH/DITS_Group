import { useSearchParams } from "react-router-dom";
import { FiMail, FiMapPin, FiPhone } from "react-icons/fi";
import { company } from "../data/company";
import PageHeader from "../components/ui/PageHeader";
import ContactForm from "../components/contact/ContactForm";
import "./Contact.css";

const infos = [
  { icon: FiMail, label: "Email", value: company.email },
  { icon: FiPhone, label: "Téléphone", value: company.phone },
  { icon: FiMapPin, label: "Adresse", value: company.address },
];

export default function Contact() {
  const [params] = useSearchParams();
  const service = params.get("service") || "";

  return (
    <>
      <PageHeader
        title="Contactez-nous"
        text="Un projet, une question, un besoin de devis ? Notre équipe vous répond."
      />
      <section className="section">
        <div className="container contact">
          <div className="contact-info">
            {infos.map(({ icon: Icon, label, value }) => (
              <div key={label} className="card info-card">
                <span className="info-icon">
                  <Icon />
                </span>
                <div>
                  <small>{label}</small>
                  <strong>{value}</strong>
                </div>
              </div>
            ))}
            <div className="contact-note">
              <h3>Réponse rapide</h3>
              <p>
                Chaque demande est étudiée par notre équipe. Nous revenons vers
                vous dans les meilleurs délais.
              </p>
            </div>
          </div>

          <div className="card contact-form-card">
            <h2>Envoyez-nous un message</h2>
            <ContactForm key={service} service={service} />
          </div>
        </div>

        <div className="container">
          <div className="card map-card">
            <iframe src={company.mapsEmbedUrl} title="Localisation de DITS Group" loading="lazy" />
            <a className="map-link" href={company.mapsUrl} target="_blank" rel="noreferrer">
              <FiMapPin /> Ouvrir dans Google Maps
            </a>
          </div>
        </div>
      </section>
    </>
  );
}