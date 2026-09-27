import { useCallback, useState } from "react";
import { FiSearch, FiTrash2 } from "react-icons/fi";
import {
  deleteSubscriber,
  getAdminSubscribers,
  updateSubscriberStatus,
} from "../../api/newsletterApi";
import { newsletterStatuses } from "../../data/statuses";
import { formatDate } from "../../utils/format";
import useFetch from "../../hooks/useFetch";
import AdminHeader from "../../components/admin/AdminHeader";
import StatusBadge from "../../components/admin/StatusBadge";
import Button from "../../components/ui/Button";
import Chips from "../../components/ui/Chips";
import DataState from "../../components/ui/DataState";
import Alert from "../../components/ui/Alert";

const tabs = Object.entries(newsletterStatuses).map(([value, { label }]) => ({
  value,
  label,
}));

export default function AdminNewsletter() {
  const [status, setStatus] = useState("");
  const [search, setSearch] = useState("");
  const [busyId, setBusyId] = useState(null);
  const [actionError, setActionError] = useState("");

  const fetcher = useCallback(() => getAdminSubscribers({ status, search }), [status, search]);
  const { data, loading, error, reload } = useFetch(fetcher);
  const subscribers = data || [];

  const run = async (item, action, confirmText) => {
    if (confirmText && !window.confirm(confirmText)) return;

    setActionError("");
    setBusyId(item.id);

    try {
      await action();
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
        title="Newsletter"
        text={`${subscribers.length} abonné(s) dans cette vue.`}
      />

      <div className="admin-toolbar">
        <Chips items={tabs} value={status} onChange={setStatus} allLabel="Tous" />
        <div className="admin-search">
          <FiSearch />
          <input
            type="search"
            placeholder="Rechercher un email..."
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
        </div>
      </div>

      <Alert>{actionError}</Alert>

      <DataState
        loading={loading}
        error={error}
        empty={!subscribers.length}
        emptyText="Aucun abonné trouvé."
      >
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Email</th>
                <th>Inscrit le</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {subscribers.map((item) => {
                const active = item.status === "ACTIVE";

                return (
                  <tr key={item.id}>
                    <td>{item.email}</td>
                    <td>{formatDate(item.subscribedAt || item.createdAt) || "—"}</td>
                    <td>
                      <StatusBadge map={newsletterStatuses} value={item.status} />
                    </td>
                    <td>
                      <div className="row-actions">
                        <Button
                          className="btn-sm"
                          variant="outline"
                          disabled={busyId === item.id}
                          onClick={() =>
                            run(item, () =>
                              updateSubscriberStatus(
                                item.id,
                                active ? "UNSUBSCRIBED" : "ACTIVE"
                              )
                            )
                          }
                        >
                          {active ? "Désabonner" : "Réactiver"}
                        </Button>
                        <Button
                          className="btn-sm"
                          variant="danger"
                          disabled={busyId === item.id}
                          onClick={() =>
                            run(
                              item,
                              () => deleteSubscriber(item.id),
                              `Supprimer ${item.email} ?`
                            )
                          }
                        >
                          <FiTrash2 />
                        </Button>
                      </div>
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