import Button from "../ui/Button";
import HeroVisual from "./HeroVisual";
import "./Hero.css";

const facts = [
  { value: "Octobre 2023", label: "Création de la société" },
  { value: "Cloud & On-premise", label: "Environnements maîtrisés" },
  { value: "National & International", label: "Périmètre d'intervention" },
];

const clients = ["FNAC DARTY", "Groupement Les Mousquetaires"];

export default function Hero() {
  return (
    <section className="hero">
      <div className="container">
        <div className="hero-inner">
          <div className="fade-up">
            <span className="hero-tag">
              Conseil et expertise en systèmes d'information
            </span>
            <h1>
              Des bases de données <span>performantes</span>, fiables et
              maîtrisées.
            </h1>
            <p className="hero-text">
              DITS Group accompagne les entreprises de tous secteurs dans la
              conception et l'administration de leurs bases de données, sur le
              cloud comme en on-premise, en France et à l'international.
            </p>
            <div className="hero-actions">
              <Button to="/services">Découvrir nos services</Button>
              <Button to="/contact" variant="light">
                Parler à un expert
              </Button>
            </div>
            <div className="hero-clients">
              <span>Ils nous font confiance</span>
              {clients.map((client) => (
                <strong key={client}>{client}</strong>
              ))}
              <span>et bien d'autres</span>
            </div>
          </div>

          <HeroVisual />
        </div>

        <div className="hero-facts">
          {facts.map(({ value, label }) => (
            <div key={value} className="hero-fact">
              <strong>{value}</strong>
              <span>{label}</span>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}