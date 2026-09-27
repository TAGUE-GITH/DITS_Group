import { useState } from "react";
import { FiEdit2, FiPlus, FiTrash2 } from "react-icons/fi";
import useFetch from "../../hooks/useFetch";
import AdminHeader from "./AdminHeader";
import Modal from "./Modal";
import ResourceForm from "./ResourceForm";
import Button from "../ui/Button";
import DataState from "../ui/DataState";
import Alert from "../ui/Alert";

export default function ResourceManager({
  title,
  text,
  newLabel,
  editLabel,
  emptyText,
  api,
  columns,
  fields,
  statusKey,
  statusLabels,
}) {
  const { data, loading, error, reload } = useFetch(api.list);
  const [editing, setEditing] = useState(null);
  const [busyId, setBusyId] = useState(null);
  const [actionError, setActionError] = useState("");

  const items = data || [];
  const isNew = editing === "new";

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

  const save = async (payload) => {
    if (isNew) await api.create(payload);
    else await api.update(editing.id, payload);

    setEditing(null);
    reload();
  };

  return (
    <>
      <AdminHeader title={title} text={text}>
        <Button onClick={() => setEditing("new")}>
          <FiPlus /> {newLabel}
        </Button>
      </AdminHeader>

      <Alert>{actionError}</Alert>

      <DataState loading={loading} error={error} empty={!items.length} emptyText={emptyText}>
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                {columns.map(({ label }) => (
                  <th key={label}>{label}</th>
                ))}
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => {
                const busy = busyId === item.id;
                const active = Boolean(item[statusKey]);

                return (
                  <tr key={item.id}>
                    {columns.map(({ label, render }) => (
                      <td key={label}>{render(item)}</td>
                    ))}
                    <td>
                      <button
                        className={`switch ${active ? "switch-on" : ""}`}
                        disabled={busy}
                        onClick={() => run(item, api.toggle)}
                      >
                        <i />
                        {statusLabels[active ? 0 : 1]}
                      </button>
                    </td>
                    <td>
                      <div className="row-actions">
                        <Button
                          className="btn-sm"
                          variant="outline"
                          onClick={() => setEditing(item)}
                        >
                          <FiEdit2 /> Modifier
                        </Button>
                        <Button
                          className="btn-sm"
                          variant="danger"
                          disabled={busy}
                          onClick={() =>
                            run(
                              item,
                              api.remove,
                              `Supprimer « ${item.title || item.name} » ?`
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

      {editing && (
        <Modal
          title={isNew ? newLabel : editLabel}
          onClose={() => setEditing(null)}
        >
          <ResourceForm
            key={isNew ? "new" : editing.id}
            fields={fields}
            item={isNew ? null : editing}
            onSubmit={save}
            onCancel={() => setEditing(null)}
          />
        </Modal>
      )}
    </>
  );
}