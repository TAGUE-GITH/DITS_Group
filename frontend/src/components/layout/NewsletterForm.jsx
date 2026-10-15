import { useState } from "react";
import { FiSend } from "react-icons/fi";
import { subscribeNewsletter } from "../../api/newsletterApi";
import useSubmit from "../../hooks/useSubmit";
import Button from "../ui/Button";
import "./NewsletterForm.css";

export default function NewsletterForm() {
  const [email, setEmail] = useState("");
  const [done, setDone] = useState(false);

  const { submit, loading, error } = useSubmit(async () => {
    await subscribeNewsletter(email);
    setEmail("");
    setDone(true);
  });

  const handleChange = (event) => {
    setEmail(event.target.value);
    setDone(false);
  };

  return (
    <form className="newsletter" onSubmit={submit}>
      <div className="newsletter-field">
        <input
          type="email"
          placeholder="Votre adresse email"
          aria-label="Adresse email"
          value={email}
          onChange={handleChange}
          required
        />
        <Button type="submit" disabled={loading}>
          <FiSend /> {loading ? "Envoi..." : "S'abonner"}
        </Button>
      </div>
      {error && <p className="newsletter-message newsletter-error">{error}</p>}
      {done && (
        <p className="newsletter-message newsletter-success">
          Merci, vous êtes inscrit à la newsletter.
        </p>
      )}
    </form>
  );
}