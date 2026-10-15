import { useMemo, useState } from "react";
import { FiCheck, FiSearch, FiSlash, FiTrash2 } from "react-icons/fi";
import {
  activateUser,
  deleteUser,
  disableUser,
  getActiveUsers,
  getAllUsers,
  getDisabledUsers,
  getPendingUsers,
} from "../../api/userApi";
import { useAuth } from "../../context/authContext";
import { userStatuses } from "../../data/statuses";
import useFetch from "../../hooks/useFetch";
import AdminHeader from "../../components/admin/AdminHeader";
import StatusBadge from "../../components/admin/StatusBadge";
import Button from "../../components/ui/Button";
import Chips from "../../components/ui/Chips";
import DataState from "../../components/ui/DataState";
import Alert from "../../components/ui/Alert";

const fetchers = {
  "": getAllUsers,
  pending: getPendingUsers,
  active: getActiveUsers,
  disabled: getDisabledUsers,
};

const tabs = [
  { value: "pending", label: "En attente" },
  { value: "active", label: "Actifs" },
  { value: "disabled", label: "Désactivés" },
];

const roleName = (role) =>
  String((typeof role === "object" ? role?.name : role) || "").replace("ROLE_", "");

export default function AdminUsers() {
  const { user: me } = useAuth();
  const [tab, setTab] = useState("pending");
  const [search, setSearch] = useState("");
  const [busyId, setBusyId] = useState(null);
  const [actionError, setActionError] = useState("");
  const { data, loading, error, reload } = useFetch(fetchers[tab]);

  const users = useMemo(() => {
    const term = search.trim().toLowerCase();

    return (data || []).filter((item) =>
      `${item.firstName} ${item.lastName} ${item.email}`
        .toLowerCase()
        .includes(term)
    );
  }, [data, search]);

  const run = async (item, action, confirmText) => {
    if (confirmText && !window.confirm(confirmText)) return;

    setActionError("");
    setBusyId(item.id);

    try {
      await action(item.id);
      reload();
    } catch (err) {
      setActionError(err.message);
    } finally {
      setBusyId(null);
    }
  };

  return (
    <>
      <AdminHeader
        title="Utilisateurs"
        text="Validez les nouveaux comptes et gérez les accès."
      />

      <div className="admin-toolbar">
        <Chips items={tabs} value={tab} onChange={setTab} allLabel="Tous" />
        <div className="admin-search">
          <FiSearch />
          <input
            type="search"
            placeholder="Rechercher un utilisateur..."
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
        </div>
      </div>

      <Alert>{actionError}</Alert>

      <DataState
        loading={loading}
        error={error}
        empty={!users.length}
        emptyText="Aucun utilisateur dans cette catégorie."
      >
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Utilisateur</th>
                <th>Email</th>
                <th>Rôle</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((item) => {
                const isSelf = item.email === me.email;
                const busy = busyId === item.id;

                return (
                  <tr key={item.id}>
                    <td>
                      <span className="cell-title">
                        {item.firstName} {item.lastName}
                      </span>
                      {item.phone && <span className="cell-sub">{item.phone}</span>}
                    </td>
                    <td>{item.email}</td>
                    <td>{roleName(item.role)}</td>
                    <td>
                      <StatusBadge map={userStatuses} value={item.status} />
                    </td>
                    <td>
                      {isSelf ? (
                        <span className="cell-sub">Votre compte</span>
                      ) : (
                        <div className="row-actions">
                          {item.status !== "ACTIVE" && (
                            <Button
                              className="btn-sm"
                              disabled={busy}
                              onClick={() => run(item, activateUser)}
                            >
                              <FiCheck />
                              {item.status === "DISABLED" ? "Réactiver" : "Activer"}
                            </Button>
                          )}
                          {item.status === "ACTIVE" && (
                            <Button
                              className="btn-sm"
                              variant="outline"
                              disabled={busy}
                              onClick={() =>
                                run(item, disableUser, `Désactiver ${item.email} ?`)
                              }
                            >
                              <FiSlash /> Désactiver
                            </Button>
                          )}
                          <Button
                            className="btn-sm"
                            variant="danger"
                            disabled={busy}
                            onClick={() =>
                              run(
                                item,
                                deleteUser,
                                `Supprimer définitivement ${item.email} ?`
                              )
                            }
                          >
                            <FiTrash2 />
                          </Button>
                        </div>
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </DataState>
    </>
  );
}