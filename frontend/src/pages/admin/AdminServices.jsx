import {
  createService,
  deleteService,
  getAdminServices,
  toggleServiceStatus,
  updateService,
} from "../../api/serviceApi";
import { excerpt } from "../../utils/format";
import ResourceManager from "../../components/admin/ResourceManager";
import TitleCell from "../../components/admin/TitleCell";

const api = {
  list: getAdminServices,
  create: createService,
  update: updateService,
  toggle: toggleServiceStatus,
  remove: deleteService,
};

const columns = [
  {
    label: "Service",
    render: (item) => (
      <TitleCell
        image={item.imageUrl || ""}
        title={item.title}
        sub={excerpt(item.description, 70)}
      />
    ),
  },
];

const fields = [
  { name: "title", label: "Titre", required: true, maxLength: 150 },
  { name: "description", label: "Description", type: "textarea", required: true },
  { name: "image", label: "Image du service", type: "file", requiredOnCreate: true },
  { name: "active", label: "Service actif", type: "checkbox", default: true },
];

export default function AdminServices() {
  return (
    <ResourceManager
      title="Services"
      text="Gérez les services affichés sur le site."
      newLabel="Nouveau service"
      editLabel="Modifier le service"
      emptyText="Aucun service pour le moment."
      api={api}
      columns={columns}
      fields={fields}
      statusKey="active"
      statusLabels={["Actif", "Inactif"]}
    />
  );
}