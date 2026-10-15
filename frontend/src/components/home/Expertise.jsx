import SectionTitle from "../ui/SectionTitle";
import "./Expertise.css";

const items = [
  {
    title: "Conseil et expertise",
    text: "Analyse de vos besoins, recommandations d'architecture et accompagnement dans vos choix technologiques.",
  },
  {
    title: "Conception de bases de données",
    text: "Modélisation et architecture adaptées à vos volumes de données et à vos usages métier.",
  },
  {
    title: "Administration et exploitation",
    text: "Supervision, sauvegardes, sécurité et optimisation des performances au quotidien.",
  },
  {
    title: "Cloud et on-premise",
    text: "Déploiement et gestion de vos environnements, dans le cloud comme dans vos propres infrastructures.",
  },
];

export default function Expertise() {
  return (
    <section className="section section-dark">
      <div className="container">
        <SectionTitle
          tag="Nos expertises"
          title="Un savoir-faire de bout en bout"
          text="De la réflexion à l'exploitation, nous sécurisons le cycle de vie de vos données."
        />
        <div className="expertise-grid">
          {items.map(({ title, text }, index) => (
            <div key={title} className="expertise-item">
              <span className="expertise-number">
                {String(index + 1).padStart(2, "0")}
              </span>
              <h3>{title}</h3>
              <p>{text}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}