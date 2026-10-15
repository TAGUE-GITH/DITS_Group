import { useCallback, useState } from "react";
import { FiEye, FiTrash2 } from "react-icons/fi";
import {
  deleteAdminRequest,
  getAdminRequests,
  updateAdminRequest,
} from "../../api/requestApi";
import { requestStatuses } from "../../data/statuses";
import { formatDate } from "../../utils/format";
import useFetch from "../../hooks/useFetch";
import AdminHeader from "../../components/admin/AdminHeader";
import ReviewModal from "../../components/admin/ReviewModal";
import StatusBadge from "../../components/admin/StatusBadge";
import Button from "../../components/ui/Button";
import Chips from "../../components/ui/Chips";
import DataState from "../../components/ui/DataState";
import Alert from "../../components/ui/Alert";

const typeLabels = {
  CONTACT: "Contact",
  DEVIS: "Devis",
  RENDEZ_VOUS: "Rendez-vous",
  AUTRE: "Autre",
};

const toChips = (map) =>
  Object.entries(map).map(([value, label]) => ({
    value,
    label: label.label || label,
  }));

export default function AdminRequests() {
  const [status, setStatus] = useState("");
  const [type, setType] = useState("");
  const [selected, setSelected] = useState(null);
  const [busyId, setBusyId] = useState(null);
  const [actionError, setActionError] = useState("");

  const fetcher = useCallback(() => getAdminRequests({ status, type }), [status, type]);
  const { data, loading, error, reload } = useFetch(fetcher);
  const requests = data || [];

  const save = async (id, values) => {
    await updateAdminRequest(id, values);
    setSelected(null);
    reload();
  };

  const remove = async (item) => {
    if (!window.confirm(`Supprimer la demande de ${item.firstName} ${item.lastName} ?`)) return;

    setActionError("");
    setBusyId(item.id);

    try {
      await deleteAdminRequest(item.id);
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
        title="Demandes"
        text="Contacts, devis et rendez-vous reçus depuis le site."
      />

      <Chips items={toChips(typeLabels)} value={type} onChange={setType} allLabel="Tous types" />
      <Chips
        items={toChips(requestStatuses)}
        value={status}
        onChange={setStatus}
        allLabel="Tous statuts"
      />

      <Alert>{actionError}</Alert>

      <DataState
        loading={loading}
        error={error}
        empty={!requests.length}
        emptyText="Aucune demande dans cette catégorie."
      >
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Demandeur</th>
                <th>Sujet</th>
                <th>Type</th>
                <th>Reçue le</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((item) => (
                <tr key={item.id}>
                  <td>
                    <span className="cell-title">
                      {item.firstName} {item.lastName}
                    </span>
                    <span className="cell-sub">{item.email}</span>
                  </td>
                  <td>{item.subject}</td>
                  <td>{typeLabels[item.type] || item.type}</td>
                  <td>{formatDate(item.createdAt) || "—"}</td>
                  <td>
                    <StatusBadge map={requestStatuses} value={item.status} />
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
                        variant="danger"
                        disabled={busyId === item.id}
                        onClick={() => remove(item)}
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
          title={selected.subject || "Demande"}
          details={[
            ["Demandeur", `${selected.firstName} ${selected.lastName}`],
            ["Email", selected.email],
            ["Téléphone", selected.phone],
            ["Entreprise", selected.company],
            ["Type", typeLabels[selected.type] || selected.type],
            ["Message", selected.message],
          ]}
          statuses={requestStatuses}
          item={selected}
          onSave={save}
          onClose={() => setSelected(null)}
        />
      )}
    </>
  );
}