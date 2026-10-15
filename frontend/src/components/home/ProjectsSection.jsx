import { getProjects } from "../../api/projectApi";
import useFetch from "../../hooks/useFetch";
import { sortByDateDesc } from "../../utils/format";
import SectionTitle from "../ui/SectionTitle";
import DataState from "../ui/DataState";
import Button from "../ui/Button";
import ProjectCard from "../cards/ProjectCard";

export default function ProjectsSection() {
  const { data, loading, error } = useFetch(getProjects);
  const projects = sortByDateDesc(data || [], "completionDate").slice(0, 3);

  if (!loading && !error && !projects.length) return null;

  return (
    <section className="section section-alt">
      <div className="container">
        <SectionTitle
          tag="Nos réalisations"
          title="Des projets concrets, menés avec rigueur"
          text="Un aperçu des missions que nous avons réalisées pour nos clients."
        />
        <DataState loading={loading} error={error} empty={!projects.length}>
          <div className="grid">
            {projects.map((project) => (
              <ProjectCard key={project.id} project={project} />
            ))}
          </div>
          <div className="section-action">
            <Button to="/realisations" variant="dark">
              Toutes les réalisations
            </Button>
          </div>
        </DataState>
      </div>
    </section>
  );
}