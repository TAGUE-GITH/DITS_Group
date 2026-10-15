import { useMemo, useState } from "react";
import { getProjects } from "../api/projectApi";
import useFetch from "../hooks/useFetch";
import { sortByDateDesc, splitTags } from "../utils/format";
import PageHeader from "../components/ui/PageHeader";
import DataState from "../components/ui/DataState";
import Chips from "../components/ui/Chips";
import usePageTitle from "../hooks/usePageTitle";
import ProjectCard from "../components/cards/ProjectCard";

export default function Projects() {
  usePageTitle("Réalisations", "Les projets menés par DITS Group.");

  const [tag, setTag] = useState("");
  const { data, loading, error } = useFetch(getProjects);

  const tags = useMemo(() => {
    const counts = {};

    (data || []).forEach((project) =>
      splitTags(project.technologies).forEach((item) => {
        counts[item] = (counts[item] || 0) + 1;
      })
    );

    return Object.entries(counts)
      .sort((a, b) => b[1] - a[1])
      .slice(0, 8)
      .map(([name]) => ({ value: name, label: name }));
  }, [data]);

  const projects = useMemo(
    () =>
      sortByDateDesc(data || [], "completionDate").filter(
        (project) => !tag || splitTags(project.technologies).includes(tag)
      ),
    [data, tag]
  );

  return (
    <>
      <PageHeader
        title="Nos réalisations"
        text="Découvrez les projets que nous avons menés pour nos clients."
      />
      <section className="section">
        <div className="container">
          <Chips
            items={tags}
            value={tag}
            onChange={setTag}
            allLabel="Toutes les technologies"
          />
          <DataState
            loading={loading}
            error={error}
            empty={!projects.length}
            emptyText="Aucune réalisation à afficher pour le moment."
          >
            <div className="grid">
              {projects.map((project) => (
                <ProjectCard key={project.id} project={project} />
              ))}
            </div>
          </DataState>
        </div>
      </section>
    </>
  );
}