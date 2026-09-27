import {
  createProject,
  deleteProject,
  getAdminProjects,
  toggleProjectPublished,
  updateProject,
} from "../../api/projectApi";
import { excerpt, formatDate } from "../../utils/format";
import ResourceManager from "../../components/admin/ResourceManager";
import TitleCell from "../../components/admin/TitleCell";

const api = {
  list: getAdminProjects,
  create: createProject,
  update: updateProject,
  toggle: toggleProjectPublished,
  remove: deleteProject,
};

const columns = [
  {
    label: "Réalisation",
    render: (item) => (
      <TitleCell
        image={item.imageUrl || ""}
        title={item.title}
        sub={excerpt(item.description, 70)}
      />
    ),
  },
  { label: "Client", render: (item) => item.clientName || "—" },
  { label: "Fin du projet", render: (item) => formatDate(item.completionDate) || "—" },
];

const fields = [
  { name: "title", label: "Titre", required: true, maxLength: 180 },
  { name: "clientName", label: "Client", half: true, maxLength: 150 },
  { name: "completionDate", label: "Date de fin", type: "date", half: true },
  { name: "description", label: "Description", type: "textarea", required: true },
  {
    name: "technologies",
    label: "Technologies (séparées par des virgules)",
    placeholder: "PostgreSQL, Java, Spring Boot",
  },
  { name: "image", label: "Image de la réalisation", type: "file", requiredOnCreate: true },
  { name: "published", label: "Publiée", type: "checkbox", default: true },
];

export default function AdminProjects() {
  return (
    <ResourceManager
      title="Réalisations"
      text="Présentez les projets menés pour vos clients."
      newLabel="Nouvelle réalisation"
      editLabel="Modifier la réalisation"
      emptyText="Aucune réalisation pour le moment."
      api={api}
      columns={columns}
      fields={fields}
      statusKey="published"
      statusLabels={["Publiée", "Masquée"]}
    />
  );
}