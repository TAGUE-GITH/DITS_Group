import { useCallback, useState } from "react";
import { FiCheck, FiTrash2, FiX } from "react-icons/fi";
import { deleteReview, getAdminReviews, updateReviewStatus } from "../../api/reviewApi";
import { reviewStatuses } from "../../data/statuses";
import { excerpt, formatDate } from "../../utils/format";
import useFetch from "../../hooks/useFetch";
import AdminHeader from "../../components/admin/AdminHeader";
import StatusBadge from "../../components/admin/StatusBadge";
import Stars from "../../components/ui/Stars";
import Button from "../../components/ui/Button";
import Chips from "../../components/ui/Chips";
import DataState from "../../components/ui/DataState";
import Alert from "../../components/ui/Alert";

const tabs = Object.entries(reviewStatuses).map(([value, { label }]) => ({ value, label }));

export default function AdminReviews() {
  const [status, setStatus] = useState("PENDING");
  const [busyId, setBusyId] = useState(null);
  const [actionError, setActionError] = useState("");

  const fetcher = useCallback(() => getAdminReviews({ status }), [status]);
  const { data, loading, error, reload } = useFetch(fetcher);
  const reviews = data || [];

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
        title="Avis clients"
        text="Validez les avis avant qu'ils n'apparaissent sur le site."
      />

      <Chips items={tabs} value={status} onChange={setStatus} allLabel="Tous" />

      <Alert>{actionError}</Alert>

      <DataState
        loading={loading}
        error={error}
        empty={!reviews.length}
        emptyText="Aucun avis dans cette catégorie."
      >
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Client</th>
                <th>Note</th>
                <th>Commentaire</th>
                <th>Reçu le</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {reviews.map((item) => {
                const busy = busyId === item.id;

                return (
                  <tr key={item.id}>
                    <td className="cell-title">{item.clientName}</td>
                    <td>
                      <Stars value={item.rating} size={14} />
                    </td>
                    <td>{excerpt(item.comment, 90)}</td>
                    <td>{formatDate(item.createdAt) || "—"}</td>
                    <td>
                      <StatusBadge map={reviewStatuses} value={item.status} />
                    </td>
                    <td>
                      <div className="row-actions">
                        {item.status !== "APPROVED" && (
                          <Button
                            className="btn-sm"
                            disabled={busy}
                            onClick={() => run(item, () => updateReviewStatus(item.id, "APPROVED"))}
                          >
                            <FiCheck /> Publier
                          </Button>
                        )}
                        {item.status !== "REJECTED" && (
                          <Button
                            className="btn-sm"
                            variant="outline"
                            disabled={busy}
                            onClick={() => run(item, () => updateReviewStatus(item.id, "REJECTED"))}
                          >
                            <FiX /> Refuser
                          </Button>
                        )}
                        <Button
                          className="btn-sm"
                          variant="danger"
                          disabled={busy}
                          onClick={() =>
                            run(item, () => deleteReview(item.id), `Supprimer l'avis de ${item.clientName} ?`)
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