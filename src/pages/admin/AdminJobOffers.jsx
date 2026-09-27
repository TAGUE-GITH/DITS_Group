import {
  createJobOffer,
  deleteJobOffer,
  getAdminJobOffers,
  toggleJobOfferPublished,
  updateJobOffer,
} from "../../api/jobApi";
import { contractLabel, formatDate } from "../../utils/format";
import ResourceManager from "../../components/admin/ResourceManager";

const api = {
  list: () => getAdminJobOffers(),
  create: createJobOffer,
  update: updateJobOffer,
  toggle: toggleJobOfferPublished,
  remove: deleteJobOffer,
};

const contractOptions = ["STAGE", "ALTERNANCE", "CDD", "CDI", "FREELANCE", "AUTRE"].map(
  (value) => [value, contractLabel(value)]
);

const columns = [
  {
    label: "Offre",
    render: (item) => (
      <>
        <span className="cell-title">{item.title}</span>
        <span className="cell-sub">{item.location}</span>
      </>
    ),
  },
  { label: "Contrat", render: (item) => contractLabel(item.contractType) },
  {
    label: "Clôture",
    render: (item) => (item.closingDate ? formatDate(item.closingDate) : "—"),
  },
];

const fields = [
  { name: "title", label: "Intitulé du poste", required: true, maxLength: 150 },
  { name: "location", label: "Lieu", half: true, placeholder: "Paris, télétravail..." },
  {
    name: "contractType",
    label: "Type de contrat",
    type: "select",
    options: contractOptions,
    default: "CDI",
    half: true,
    required: true,
  },
  { name: "description", label: "Description", type: "textarea", required: true },
  { name: "requirements", label: "Profil et compétences", type: "textarea" },
  { name: "closingDate", label: "Date de clôture", type: "date" },
  { name: "published", label: "Offre publiée", type: "checkbox", default: true },
];

export default function AdminJobOffers() {
  return (
    <ResourceManager
      title="Offres d'emploi"
      text="Publiez et gérez les offres de la page Carrières."
      newLabel="Nouvelle offre"
      editLabel="Modifier l'offre"
      emptyText="Aucune offre pour le moment."
      api={api}
      columns={columns}
      fields={fields}
      statusKey="published"
      statusLabels={["Publiée", "Brouillon"]}
    />
  );
}