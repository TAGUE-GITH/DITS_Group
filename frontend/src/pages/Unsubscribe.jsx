import { useState } from "react";
import { unsubscribeNewsletter } from "../api/newsletterApi";
import useSubmit from "../hooks/useSubmit";
import PageHeader from "../components/ui/PageHeader";
import Field from "../components/ui/Field";
import Button from "../components/ui/Button";
import Alert from "../components/ui/Alert";

export default function Unsubscribe() {
  const [email, setEmail] = useState("");
  const [done, setDone] = useState(false);

  const { submit, loading, error } = useSubmit(async () => {
    await unsubscribeNewsletter(email);
    setDone(true);
  });

  return (
    <>
      <PageHeader
        title="Se désabonner"
        text="Saisissez votre adresse email pour ne plus recevoir notre newsletter."
      />
      <section className="section">
        <div className="container" style={{ maxWidth: 520 }}>
          <div className="card">
            {done ? (
              <Alert type="success">
                Vous êtes désabonné de la newsletter.
              </Alert>
            ) : (
              <form onSubmit={submit}>
                <Alert>{error}</Alert>
                <Field
                  label="Adresse email"
                  id="email"
                  type="email"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  required
                />
                <Button type="submit" block disabled={loading}>
                  {loading ? "Traitement..." : "Me désabonner"}
                </Button>
              </form>
            )}
          </div>
        </div>
      </section>
    </>
  );
}