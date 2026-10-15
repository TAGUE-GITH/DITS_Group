import { applicationStatuses } from "../../data/statuses";
import { formatDate } from "../../utils/format";
import useForm from "../../hooks/useForm";
import useSubmit from "../../hooks/useSubmit";
import Modal from "./Modal";
import Field from "../ui/Field";
import Button from "../ui/Button";
import Alert from "../ui/Alert";

export default function ApplicationModal({ application, onSave, onClose }) {
  const { values, handleChange } = useForm({
    status: application.status || "PENDING",
    adminNote: application.adminNote || "",
  });

  const { submit, loading, error } = useSubmit(() => onSave(application.id, values));

  return (
    <Modal
      title={`${application.firstName} ${application.lastName}`}
      onClose={onClose}
    >
      <dl className="detail-list">
        <div>
          <dt>Email</dt>
          <dd>{application.email}</dd>
        </div>
        {application.phone && (
          <div>
            <dt>Téléphone</dt>
            <dd>{application.phone}</dd>
          </div>
        )}
        {application.createdAt && (
          <div>
            <dt>Reçue le</dt>
            <dd>{formatDate(application.createdAt)}</dd>
          </div>
        )}
        {application.message && (
          <div>
            <dt>Message</dt>
            <dd>{application.message}</dd>
          </div>
        )}
      </dl>

      <form onSubmit={submit}>
        <Alert>{error}</Alert>
        <div className="form-grid">
          <div className="form-full">
            <Field
              label="Statut"
              id="status"
              as="select"
              value={values.status}
              onChange={handleChange}
            >
              {Object.entries(applicationStatuses).map(([value, { label }]) => (
                <option key={value} value={value}>
                  {label}
                </option>
              ))}
            </Field>
          </div>
          <div className="form-full">
            <Field
              label="Note interne"
              id="adminNote"
              as="textarea"
              value={values.adminNote}
              onChange={handleChange}
            />
          </div>
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