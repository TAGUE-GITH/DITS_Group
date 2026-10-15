import { useState } from "react";
import { FiUpload } from "react-icons/fi";
import { applyToJobOffer } from "../../api/jobApi";
import useForm from "../../hooks/useForm";
import useSubmit from "../../hooks/useSubmit";
import Field from "../ui/Field";
import Button from "../ui/Button";
import Alert from "../ui/Alert";
import "./ApplicationForm.css";

const MAX_SIZE = 5 * 1024 * 1024;

export default function ApplicationForm({ jobOfferId }) {
  const [cv, setCv] = useState(null);
  const [sent, setSent] = useState(false);
  const { values, handleChange } = useForm({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    message: "",
  });

  const { submit, loading, error } = useSubmit(async () => {
    if (!cv) throw new Error("Veuillez joindre votre CV.");
    if (cv.size > MAX_SIZE) throw new Error("Le CV ne doit pas dépasser 5 Mo.");

    await applyToJobOffer(jobOfferId, { ...values, cv });
    setSent(true);
  });

  if (sent) {
    return (
      <Alert type="success">
        Merci ! Votre candidature a bien été envoyée. Nous reviendrons vers
        vous rapidement.
      </Alert>
    );
  }

  return (
    <form onSubmit={submit}>
      <Alert>{error}</Alert>
      <div className="form-row">
        <Field
          label="Prénom"
          id="firstName"
          value={values.firstName}
          onChange={handleChange}
          required
        />
        <Field
          label="Nom"
          id="lastName"
          value={values.lastName}
          onChange={handleChange}
          required
        />
      </div>
      <Field
        label="Adresse email"
        id="email"
        type="email"
        value={values.email}
        onChange={handleChange}
        required
      />
      <Field
        label="Téléphone"
        id="phone"
        type="tel"
        value={values.phone}
        onChange={handleChange}
      />
      <Field
        label="Message"
        id="message"
        as="textarea"
        placeholder="Présentez-vous en quelques lignes"
        value={values.message}
        onChange={handleChange}
      />

      <div className="field">
        <label htmlFor="cv">CV (PDF, DOC, DOCX)</label>
        <label htmlFor="cv" className="file-input">
          <FiUpload />
          <span>{cv ? cv.name : "Choisir un fichier"}</span>
          <input
            id="cv"
            type="file"
            accept=".pdf,.doc,.docx"
            onChange={(event) => setCv(event.target.files[0] || null)}
          />
        </label>
      </div>

      <Button type="submit" block disabled={loading}>
        {loading ? "Envoi..." : "Envoyer ma candidature"}
      </Button>
    </form>
  );
}