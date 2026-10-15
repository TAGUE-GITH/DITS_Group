import {
  FiAward,
  FiBriefcase,
  FiFile,
  FiFileText,
  FiFolder,
  FiGrid,
  FiInbox,
  FiLayers,
  FiMail,
  FiMessageSquare,
  FiStar,
  FiUsers,
} from "react-icons/fi";

export const adminLinks = [
  { to: "/admin", label: "Tableau de bord", icon: FiGrid, end: true },
  { to: "/admin/services", label: "Services", icon: FiLayers },
  { to: "/admin/realisations", label: "Réalisations", icon: FiFolder },
  { to: "/admin/articles", label: "Articles", icon: FiFileText },
  { to: "/admin/partenaires", label: "Partenaires", icon: FiAward },
  { to: "/admin/offres", label: "Offres d'emploi", icon: FiBriefcase },
  { to: "/admin/candidatures", label: "Candidatures", icon: FiInbox },
  { to: "/admin/demandes", label: "Demandes", icon: FiMessageSquare },
  { to: "/admin/avis", label: "Avis clients", icon: FiStar },
  { to: "/admin/newsletter", label: "Newsletter", icon: FiMail },
  { to: "/admin/documents", label: "Documents", icon: FiFile },
  { to: "/admin/utilisateurs", label: "Utilisateurs", icon: FiUsers },
];