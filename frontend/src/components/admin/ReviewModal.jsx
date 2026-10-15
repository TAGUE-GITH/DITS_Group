import useForm from "../../hooks/useForm";
import useSubmit from "../../hooks/useSubmit";
import Modal from "./Modal";
import Field from "../ui/Field";
import Button from "../ui/Button";
import Alert from "../ui/Alert";

export default function ReviewModal({ title, details, statuses, item, onSave, onClose }) {
  const { values, handleChange } = useForm({
    status: item.status || Object.keys(statuses)[0],
    adminNote: item.adminNote || "",
  });

  const { submit, loading, error } = useSubmit(() => onSave(item.id, values));

  return (
    <Modal title={title} onClose={onClose}>
      <dl className="detail-list">
        {details
          .filter(([, value]) => value)
          .map(([label, value]) => (
            <div key={label}>
              <dt>{label}</dt>
              <dd>{value}</dd>
            </div>
          ))}
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
              {Object.entries(statuses).map(([value, { label }]) => (
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