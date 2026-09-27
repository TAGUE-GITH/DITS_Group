import { useState } from "react";
import { getActiveUsers } from "../../api/userApi";
import useFetch from "../../hooks/useFetch";
import useForm from "../../hooks/useForm";
import useSubmit from "../../hooks/useSubmit";
import { formatSize } from "../../utils/format";
import Modal from "./Modal";
import Field from "../ui/Field";
import Button from "../ui/Button";
import Alert from "../ui/Alert";

const MAX_SIZE = 10 * 1024 * 1024;

export default function DocumentModal({ doc, onSave, onClose }) {
  const isNew = !doc;
  const [file, setFile] = useState(null);
  const [fileError, setFileError] = useState("");
  const { data: users } = useFetch(getActiveUsers);
  const { values, handleChange } = useForm({
    title: doc?.title || "",
    description: doc?.description || "",
    userId: "",
  });

  const handleFile = (event) => {
    const selected = event.target.files[0] || null;

    if (selected && selected.size > MAX_SIZE) {
      setFile(null);
      setFileError(
        `Ce fichier fait ${formatSize(selected.size)}. La taille maximale est de ${formatSize(MAX_SIZE)}.`
      );
      event.target.value = "";
      return;
    }

    setFileError("");
    setFile(selected);
  };

  const { submit, loading, error } = useSubmit(async () => {
    if (isNew && !values.userId) throw new Error("Choisissez le client destinataire.");
    if (isNew && !file) throw new Error("Choisissez un fichier de 10 Mo maximum.");

    await onSave({ ...values, file });
  });

  return (
    <Modal title={isNew ? "Nouveau document" : "Modifier le document"} onClose={onClose}>
      <form onSubmit={submit}>
        <Alert>{fileError || error}</Alert>
        <div className="form-grid">
          <div className="form-full">
            <Field
              label="Titre"
              id="title"
              value={values.title}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-full">
            <Field
              label="Description"
              id="description"
              as="textarea"
              value={values.description}
              onChange={handleChange}
            />
          </div>
          {isNew && (
            <>
              <div className="form-full">
                <Field
                  label="Client"
                  id="userId"
                  as="select"
                  value={values.userId}
                  onChange={handleChange}
                  required
                >
                  <option value="">Choisir un client</option>
                  {(users || []).map((user) => (
                    <option key={user.id} value={user.id}>
                      {user.firstName} {user.lastName} ({user.email})
                    </option>
                  ))}
                </Field>
              </div>
              <div className="form-full">
                <Field
                  label="Fichier (10 Mo maximum)"
                  id="file"
                  type="file"
                  onChange={handleFile}
                  required
                />
              </div>
            </>
          )}
        </div>
        <div className="form-actions">
          <Button type="button" variant="outline" onClick={onClose}>
            Annuler
          </Button>
          <Button type="submit" disabled={loading}>
            {loading ? "Enregistrement..." : "Enregistrer"}
          </Button>
        </div>
      </form>
    </Modal>
  );
}