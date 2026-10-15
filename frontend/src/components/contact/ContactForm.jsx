import { useState } from "react";
import { createRequest } from "../../api/requestApi";
import useForm from "../../hooks/useForm";
import useSubmit from "../../hooks/useSubmit";
import Field from "../ui/Field";
import Button from "../ui/Button";
import Alert from "../ui/Alert";
import "./ContactForm.css";

const requestTypes = [
  { value: "CONTACT", label: "Contact" },
  { value: "DEVIS", label: "Devis" },
  { value: "RENDEZ_VOUS", label: "Rendez-vous" },
  { value: "AUTRE", label: "Autre" },
];

export default function ContactForm({ service }) {
  const [sent, setSent] = useState(false);
  const { values, handleChange } = useForm({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    company: "",
    type: service ? "DEVIS" : "CONTACT",
    subject: service ? `Demande de devis : ${service}` : "",
    message: "",
  });

  const { submit, loading, error } = useSubmit(async () => {
    await createRequest(values);
    setSent(true);
  });

  if (sent) {
    return (
      <Alert type="success">
        Merci ! Votre demande a bien été envoyée. Nous revenons vers vous dans
        les meilleurs délais.
      </Alert>
    );
  }

  return (
    <form onSubmit={submit}>
      <Alert>{error}</Alert>

      <div className="field">
        <span className="field-label">Type de demande</span>
        <div className="type-options">
          {requestTypes.map(({ value, label }) => (
            <label
              key={value}
              className={`type-option ${
                values.type === value ? "type-option-active" : ""
              }`}
            >
              <input
                type="radio"
                name="type"
                value={value}
                checked={values.type === value}
                onChange={handleChange}
              />
              {label}
            </label>
          ))}
        </div>
      </div>

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

      <div className="form-row">
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
      </div>

      <Field
        label="Entreprise"
        id="company"
        value={values.company}
        onChange={handleChange}
      />
      <Field
        label="Sujet"
        id="subject"
        value={values.subject}
        onChange={handleChange}
        required
      />
      <Field
        label="Message"
        id="message"
        as="textarea"
        placeholder="Décrivez votre besoin"
        value={values.message}
        onChange={handleChange}
        required
      />

      <Button type="submit" block disabled={loading}>
        {loading ? "Envoi..." : "Envoyer ma demande"}
      </Button>
    </form>
  );
}