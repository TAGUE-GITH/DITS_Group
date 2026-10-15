import {
  createArticle,
  deleteArticle,
  getAdminArticles,
  toggleArticleStatus,
  updateArticle,
} from "../../api/articleApi";
import { formatDate } from "../../utils/format";
import ResourceManager from "../../components/admin/ResourceManager";
import TitleCell from "../../components/admin/TitleCell";

const api = {
  list: getAdminArticles,
  create: createArticle,
  update: updateArticle,
  toggle: toggleArticleStatus,
  remove: deleteArticle,
};

const columns = [
  {
    label: "Article",
    render: (item) => (
      <TitleCell
        image={item.imageUrl || ""}
        title={item.title}
        sub={item.author && `Par ${item.author}`}
      />
    ),
  },
  { label: "Publication", render: (item) => formatDate(item.publicationDate) || "—" },
];

const fields = [
  { name: "title", label: "Titre", required: true, maxLength: 200 },
  { name: "author", label: "Auteur", required: true, half: true, maxLength: 150 },
  { name: "publicationDate", label: "Date de publication", type: "datetime-local", half: true },
  { name: "summary", label: "Résumé", type: "textarea", required: true, maxLength: 500 },
  { name: "content", label: "Contenu", type: "textarea", required: true },
  { name: "image", label: "Image de l'article", type: "file", requiredOnCreate: true },
  { name: "published", label: "Publié", type: "checkbox", default: false },
];

export default function AdminArticles() {
  return (
    <ResourceManager
      title="Articles"
      text="Rédigez et publiez vos actualités."
      newLabel="Nouvel article"
      editLabel="Modifier l'article"
      emptyText="Aucun article pour le moment."
      api={api}
      columns={columns}
      fields={fields}
      statusKey="published"
      statusLabels={["Publié", "Brouillon"]}
    />
  );
}