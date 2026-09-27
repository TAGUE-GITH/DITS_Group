import { useCallback, useState } from "react";
import { FiDownload, FiEye, FiTrash2 } from "react-icons/fi";
import {
  deleteApplication,
  downloadApplicationCv,
  getAdminApplications,
  updateApplication,
} from "../../api/jobApi";
import { applicationStatuses } from "../../data/statuses";
import { formatDate } from "../../utils/format";
import useFetch from "../../hooks/useFetch";
import AdminHeader from "../../components/admin/AdminHeader";
import ReviewModal from "../../components/admin/ReviewModal";
import StatusBadge from "../../components/admin/StatusBadge";
import Button from "../../components/ui/Button";
import Chips from "../../components/ui/Chips";
import DataState from "../../components/ui/DataState";
import Alert from "../../components/ui/Alert";

const tabs = Object.entries(applicationStatuses).map(([value, { label }]) => ({
  value,
  label,
}));

export default function AdminApplications() {
  const [status, setStatus] = useState("");
  const [selected, setSelected] = useState(null);
  const [busyId, setBusyId] = useState(null);
  const [actionError, setActionError] = useState("");

  const fetcher = useCallback(() => getAdminApplications({ status }), [status]);
  const { data, loading, error, reload } = useFetch(fetcher);
  const applications = data || [];

  const run = async (item, action, confirmText) => {
    if (confirmText && !window.confirm(confirmText)) return;

    setActionError("");
    setBusyId(item.id);

    try {
      await action(item);
      reload();
    } catch (err) {
      setActionError(err.message);
    } finally {
      setBusyId(null);
    }
  };

  const save = async (id, values) => {
    await updateApplication(id, values);
    setSelected(null);
    reload();
  };

  return (
    <>
      <AdminHeader
        title="Candidatures"
        text="Étudiez les candidatures reçues et téléchargez les CV."
      />

      <Chips items={tabs} value={status} onChange={setStatus} allLabel="Toutes" />

      <Alert>{actionError}</Alert>

      <DataState
        loading={loading}
        error={error}
        empty={!applications.length}
        emptyText="Aucune candidature dans cette catégorie."
      >
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Candidat</th>
                <th>Offre</th>
                <th>Reçue le</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((item) => (
                <tr key={item.id}>
                  <td>
                    <span className="cell-title">
                      {item.firstName} {item.lastName}
                    </span>
                    <span className="cell-sub">{item.email}</span>
                  </td>
                  <td>{item.jobOfferTitle || item.jobOffer?.title || "—"}</td>
                  <td>{formatDate(item.createdAt) || "—"}</td>
                  <td>
                    <StatusBadge map={applicationStatuses} value={item.status} />
                  </td>
                  <td>
                    <div className="row-actions">
                      <Button
                        className="btn-sm"
                        variant="outline"
                        onClick={() => setSelected(item)}
                      >
                        <FiEye /> Traiter
                      </Button>
                      <Button
                        className="btn-sm"
                        variant="outline"
                        disabled={busyId === item.id}
                        onClick={() =>
                          run(item, () =>
                            downloadApplicationCv(
                              item.id,
                              `CV-${item.lastName}-${item.firstName}`
                            )
                          )
                        }
                      >
                        <FiDownload /> CV
                      </Button>
                      <Button
                        className="btn-sm"
                        variant="danger"
                        disabled={busyId === item.id}
                        onClick={() =>
                          run(
                            item,
                            () => deleteApplication(item.id),
                            `Supprimer la candidature de ${item.firstName} ${item.lastName} ?`
                          )
                        }
                      >
                        <FiTrash2 />
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </DataState>

      {selected && (
        <ReviewModal
          title={`${selected.firstName} ${selected.lastName}`}
          details={[
            ["Email", selected.email],
            ["Téléphone", selected.phone],
            ["Reçue le", formatDate(selected.createdAt)],
            ["Message", selected.message],
          ]}
          statuses={applicationStatuses}
          item={selected}
          onSave={save}
          onClose={() => setSelected(null)}
        />
      )}
    </>
  );
}