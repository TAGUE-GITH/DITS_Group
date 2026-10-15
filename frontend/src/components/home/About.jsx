import {
  FiCalendar,
  FiCheckCircle,
  FiCloud,
  FiDatabase,
  FiGlobe,
} from "react-icons/fi";
import "./About.css";

const points = [
  "Conseil et expertise sur vos systèmes informatiques",
  "Conception et administration de bases de données",
  "Environnements cloud et on-premise",
  "Interventions au niveau national et international",
];

const cards = [
  { icon: FiCalendar, title: "Octobre 2023", text: "Création de DITS Group" },
  { icon: FiDatabase, title: "Bases de données", text: "Conception et administration" },
  { icon: FiCloud, title: "Cloud & On-premise", text: "Selon votre infrastructure" },
  { icon: FiGlobe, title: "Sans frontières", text: "National et international" },
];

export default function About() {
  return (
    <section className="section section-alt">
      <div className="container about">
        <div className="about-text">
          <span className="tag">Qui sommes-nous</span>
          <h2>Le conseil et l'expertise au service de vos systèmes d'information</h2>
          <p>
            Fondée en octobre 2023, DITS Group propose aux entreprises de tous
            secteurs d'activité une offre de conseil et d'expertise autour des
            systèmes informatiques, avec une spécialité : la conception et
            l'administration de bases de données.
          </p>
          <p>
            Nous intervenons sur le cloud comme en on-premise, en France et à
            l'international, pour garantir des données disponibles,
            performantes et sécurisées. Des enseignes comme FNAC DARTY et le
            Groupement Les Mousquetaires nous font déjà confiance.
          </p>
          <ul className="about-list">
            {points.map((point) => (
              <li key={point}>
                <FiCheckCircle /> {point}
              </li>
            ))}
          </ul>
        </div>

        <div className="about-cards">
          {cards.map(({ icon: Icon, title, text }) => (
            <div key={title} className="card card-hover about-card">
              <span className="about-icon">
                <Icon />
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