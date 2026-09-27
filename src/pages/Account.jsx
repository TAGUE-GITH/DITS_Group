import { useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import {
  FiGrid,
  FiLayers,
  FiLogOut,
  FiMessageSquare,
  FiSearch,
} from "react-icons/fi";
import { getMyDocuments } from "../api/documentApi";
import { useAuth } from "../context/authContext";
import useFetch from "../hooks/useFetch";
import { sortByDateDesc } from "../utils/format";
import PageHeader from "../components/ui/PageHeader";
import DataState from "../components/ui/DataState";
import Button from "../components/ui/Button";
import DocumentCard from "../components/account/DocumentCard";
import "./Account.css";
import usePageTitle from "../hooks/usePageTitle";
import ReviewCard from "../components/account/ReviewCard";

export default function Account() {
  usePageTitle("Mon espace", "Gérez vos documents et suivez vos échanges avec DITS Group.");

  const { user, isAdmin, logout } = useAuth();
  const navigate = useNavigate();
  const [search, setSearch] = useState("");
  const { data, loading, error } = useFetch(getMyDocuments);

  const documents = useMemo(() => {
    const term = search.trim().toLowerCase();

    return sortByDateDesc(data || [], "createdAt").filter((doc) =>
      `${doc.title} ${doc.description || ""}`.toLowerCase().includes(term)
    );
  }, [data, search]);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <>
      <PageHeader
        title="Mon espace"
        text="Retrouvez vos documents et suivez vos échanges avec DITS Group."
      />
      <section className="section">
        <div className="container account">
          <aside className="account-side">
            <div className="card profile">
              <span className="avatar">{user.email?.charAt(0)}</span>
              <strong>{user.email}</strong>
              <span className="badge">{isAdmin ? "Administrateur" : "Client"}</span>
              <Button variant="outline" block onClick={handleLogout}>
                <FiLogOut /> Déconnexion
              </Button>
            </div>

            <div className="card quick-links">
              <Link to="/contact">
                <FiMessageSquare /> Nouvelle demande
              </Link>
              <Link to="/services">
                <FiLayers /> Découvrir nos services
              </Link>
              {isAdmin && (
                <Link to="/admin">
                  <FiGrid /> Administration
                </Link>
              )}
            </div>
            <ReviewCard />
          </aside>

          <div>
            <div className="account-head">
              <h2>Mes documents ({data?.length || 0})</h2>
              <div className="doc-search">
                <FiSearch />
                <input
                  type="search"
                  placeholder="Rechercher un document..."
                  value={search}
                  onChange={(event) => setSearch(event.target.value)}
                />
              </div>
            </div>

            <DataState
              loading={loading}
              error={error}
              empty={!documents.length}
              emptyText="Aucun document pour le moment. Les documents partagés par DITS Group apparaîtront ici."
            >
              <div className="doc-list">
                {documents.map((doc) => (
                  <DocumentCard key={doc.id} doc={doc} />
                ))}
              </div>
            </DataState>
          </div>
        </div>
      </section>
    </>
  );
}