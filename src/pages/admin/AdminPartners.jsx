import {
  createPartner,
  deletePartner,
  getAdminPartners,
  togglePartnerPublished,
  updatePartner,
} from "../../api/partnerApi";
import ResourceManager from "../../components/admin/ResourceManager";
import TitleCell from "../../components/admin/TitleCell";

const api = {
  list: getAdminPartners,
  create: createPartner,
  update: updatePartner,
  toggle: togglePartnerPublished,
  remove: deletePartner,
};

const columns = [
  {
    label: "Partenaire",
    render: (item) => (
      <TitleCell image={item.logoUrl || ""} title={item.name} sub={item.websiteUrl} />
    ),
  },
];

const fields = [
  { name: "name", label: "Nom", required: true, maxLength: 180 },
  { name: "description", label: "Description", type: "textarea" },
  { name: "logo", label: "Logo", type: "file", half: true, requiredOnCreate: true },
  { name: "websiteUrl", label: "Site web", type: "url", half: true, placeholder: "https://..." },
  { name: "published", label: "Publié", type: "checkbox", default: false },
];

export default function AdminPartners() {
  return (
    <ResourceManager
      title="Partenaires"
      text="Gérez les partenaires affichés sur le site."
      newLabel="Nouveau partenaire"
      editLabel="Modifier le partenaire"
      emptyText="Aucun partenaire pour le moment."
      api={api}
      columns={columns}
      fields={fields}
      statusKey="published"
      statusLabels={["Publié", "Masqué"]}
    />
  );
}