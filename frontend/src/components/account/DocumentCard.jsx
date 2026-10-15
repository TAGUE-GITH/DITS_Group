import { FiDownload, FiFileText } from "react-icons/fi";
import { downloadMyDocument } from "../../api/documentApi";
import useSubmit from "../../hooks/useSubmit";
import { formatDate, formatSize } from "../../utils/format";
import Button from "../ui/Button";
import Alert from "../ui/Alert";

export default function DocumentCard({ doc }) {
  const fileName = doc.originalFileName || doc.fileName || doc.title;
  const size = formatSize(doc.fileSize || doc.size);

  const { submit, loading, error } = useSubmit(() =>
    downloadMyDocument(doc.id, fileName)
  );

  return (
    <div className="card doc-card">
      <div className="doc">
        <span className="doc-icon">
          <FiFileText />
        </span>
        <div className="doc-info">
          <h3>{doc.title}</h3>
          {doc.description && <p>{doc.description}</p>}
          <div className="doc-meta">
            {doc.createdAt && <span>{formatDate(doc.createdAt)}</span>}
            {size && <span>{size}</span>}
          </div>
        </div>
        <Button variant="dark" onClick={submit} disabled={loading}>
          <FiDownload /> {loading ? "Téléchargement..." : "Télécharger"}
        </Button>
      </div>
      <Alert>{error}</Alert>
    </div>
  );
}