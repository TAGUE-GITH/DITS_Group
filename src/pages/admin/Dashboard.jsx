import { Link } from "react-router-dom";
import { FiBriefcase, FiInbox, FiMail, FiUserCheck } from "react-icons/fi";
import { getDashboardStats } from "../../api/dashboardApi";
import useFetch from "../../hooks/useFetch";
import AdminHeader from "../../components/admin/AdminHeader";
import DonutChart from "../../components/admin/DonutChart";
import BarChart from "../../components/admin/BarChart";
import DataState from "../../components/ui/DataState";
import "./Dashboard.css";

const GOLD = "var(--gold)";
const BLUE = "#3b82f6";
const GREEN = "var(--success)";
const RED = "var(--danger)";
const PURPLE = "#8b5cf6";
const TEAL = "#14b8a6";

const kpis = [
  { key: "pendingUsers", label: "Comptes à valider", icon: FiUserCheck, to: "/admin/utilisateurs", alert: true },
  { key: "pendingRequests", label: "Demandes en attente", icon: FiInbox, alert: true },
  { key: "pendingApplications", label: "Candidatures à traiter", icon: FiBriefcase, alert: true },
  { key: "activeNewsletterSubscribers", label: "Abonnés newsletter", icon: FiMail },
];

const donutSections = [
  {
    title: "Utilisateurs",
    totalLabel: "Comptes",
    items: [
      ["activeUsers", "Actifs", GREEN],
      ["pendingUsers", "En attente", GOLD],
      ["disabledUsers", "Désactivés", RED],
    ],
  },
  {
    title: "Demandes",
    totalLabel: "Demandes",
    items: [
      ["pendingRequests", "En attente", GOLD],
      ["inProgressRequests", "En cours", BLUE],
      ["processedRequests", "Traitées", GREEN],
      ["rejectedRequests", "Refusées", RED],
    ],
  },
  {
    title: "Candidatures",
    totalLabel: "Candidatures",
    items: [
      ["pendingApplications", "En attente", GOLD],
      ["inReviewApplications", "En étude", BLUE],
      ["acceptedApplications", "Acceptées", GREEN],
      ["rejectedApplications", "Refusées", RED],
    ],
  },
  {
    title: "Offres d'emploi",
    totalLabel: "Offres",
    items: [
      ["publishedJobOffers", "Publiées", GREEN],
      ["unpublishedJobOffers", "Masquées", RED],
    ],
  },
  {
    title: "Newsletter",
    totalLabel: "Abonnés",
    items: [
      ["activeNewsletterSubscribers", "Actifs", GREEN],
      ["unsubscribedNewsletterSubscribers", "Désabonnés", RED],
    ],
  },
];

const contentBars = [
  ["totalServices", "Services", GOLD],
  ["totalProjects", "Réalisations", BLUE],
  ["totalArticles", "Articles", GREEN],
  ["totalPartners", "Partenaires", PURPLE],
  ["totalDocuments", "Documents", TEAL],
];

export default function Dashboard() {
  const { data: stats, loading, error } = useFetch(getDashboardStats);

  return (
    <>
      <AdminHeader
        title="Tableau de bord"
        text="Vue d'ensemble de l'activité de DITS Group."
      />
      <DataState loading={loading} error={error} empty={!stats}>
        <div className="kpis">
          {kpis.map(({ key, label, icon: Icon, to, alert }) => {
            const value = stats?.[key] ?? 0;
            const Tag = to ? Link : "div";

            return (
              <Tag
                key={key}
                to={to}
                className={`card kpi ${alert && value > 0 ? "kpi-alert" : ""}`}
              >
                <span className="kpi-icon">
                  <Icon />
                </span>
                <div>
                  <strong>{value}</strong>
                  <span>{label}</span>
                </div>
              </Tag>
            );
          })}
        </div>

        <div className="card chart-card chart-card-wide">
          <h3>Contenus publiés</h3>
          <BarChart items={contentBars} stats={stats || {}} />
        </div>

        <div className="chart-grid">
          {donutSections.map(({ title, totalLabel, items }) => (
            <div key={title} className="card chart-card">
              <h3>{title}</h3>
              <DonutChart items={items} stats={stats || {}} totalLabel={totalLabel} />
            </div>
          ))}
        </div>
      </DataState>
    </>
  );
}