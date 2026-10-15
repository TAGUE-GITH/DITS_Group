import { useState } from "react";
import { FiDownload, FiEdit2, FiPlus, FiTrash2 } from "react-icons/fi";
import {
  deleteDocument,
  downloadAdminDocument,
  getAdminDocuments,
  updateDocument,
  uploadDocument,
} from "../../api/documentApi";
import { formatDate, formatSize } from "../../utils/format";
import useFetch from "../../hooks/useFetch";
import AdminHeader from "../../components/admin/AdminHeader";
import DocumentModal from "../../components/admin/DocumentModal";
import Button from "../../components/ui/Button";
import DataState from "../../components/ui/DataState";
import Alert from "../../components/ui/Alert";

const ownerOf = (doc) =>
  doc.userEmail || doc.user?.email || doc.ownerEmail || doc.userId || "—";

export default function AdminDocuments() {
  const { data, loading, error, reload } = useFetch(getAdminDocuments);
  const [editing, setEditing] = useState(null);
  const [busyId, setBusyId] = useState(null);
  const [actionError, setActionError] = useState("");

  const documents = data || [];
  const isNew = editing === "new";

  const run = async (doc, action, confirmText) => {
    if (confirmText && !window.confirm(confirmText)) return;

    setActionError("");
    setBusyId(doc.id);

    try {
      await action();
      reload();
    } catch (err) {
      setActionError(err.message);
    } finally {
      setBusyId(null);
    }
  };

  const save = async ({ title, description, userId, file }) => {
    if (isNew) await uploadDocument({ title, description, userId, file });
    else await updateDocument(editing.id, { title, description });

    setEditing(null);
    reload();
  };

  return (
    <>
      <AdminHeader
        title="Documents"
        text="Déposez des documents dans l'espace personnel de vos clients."
      >
        <Button onClick={() => setEditing("new")}>
          <FiPlus /> Nouveau document
        </Button>
      </AdminHeader>

      <Alert>{actionError}</Alert>

      <DataState
        loading={loading}
        error={error}
        empty={!documents.length}
        emptyText="Aucun document déposé pour le moment."
      >
        <div className="admin-table-wrap">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Document</th>
                <th>Client</th>
                <th>Ajouté le</th>
                <th>Taille</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {documents.map((doc) => {
                const fileName = doc.originalFileName || doc.fileName || doc.title;

                return (
                  <tr key={doc.id}>
                    <td>
                      <span className="cell-title">{doc.title}</span>
                      {doc.description && <span className="cell-sub">{doc.description}</span>}
                    </td>
                    <td>{ownerOf(doc)}</td>
                    <td>{formatDate(doc.createdAt) || "—"}</td>
                    <td>{formatSize(doc.fileSize || doc.size) || "—"}</td>
                    <td>
                      <div className="row-actions">
                        <Button
                          className="btn-sm"
                          variant="outline"
                          disabled={busyId === doc.id}
                          onClick={() =>
                            run(doc, () => downloadAdminDocument(doc.id, fileName))
                          }
                        >
                          <FiDownload />
                        </Button>
                        <Button
                          className="btn-sm"
                          variant="outline"
                          onClick={() => setEditing(doc)}
                        >
                          <FiEdit2 /> Modifier
                        </Button>
                        <Button
                          className="btn-sm"
                          variant="danger"
                          disabled={busyId === doc.id}
                          onClick={() =>
                            run(
                              doc,
                              () => deleteDocument(doc.id),
                              `Supprimer « ${doc.title} » ?`
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
        <DocumentModal
          key={isNew ? "new" : editing.id}
          doc={isNew ? null : editing}
          onSave={save}
          onClose={() => setEditing(null)}
        />
      )}
    </>
  );
}