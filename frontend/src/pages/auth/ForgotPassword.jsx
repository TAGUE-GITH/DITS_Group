import { useState } from "react";
import { Link } from "react-router-dom";
import { forgotPassword } from "../../api/authApi";
import useForm from "../../hooks/useForm";
import useSubmit from "../../hooks/useSubmit";
import AuthLayout from "../../components/auth/AuthLayout";
import Field from "../../components/ui/Field";
import Button from "../../components/ui/Button";
import Alert from "../../components/ui/Alert";

export default function ForgotPassword() {
  const [sent, setSent] = useState(false);
  const { values, handleChange } = useForm({ email: "" });

  const { submit, loading, error } = useSubmit(async () => {
    await forgotPassword(values.email);
    setSent(true);
  });

  return (
    <AuthLayout
      variant="lock"
      visualTitle="Accès perdu ? Pas de panique"
      visualText="Recevez un lien sécurisé par email pour choisir un nouveau mot de passe en quelques secondes."
      title="Mot de passe oublié"
      subtitle="Saisissez votre email, nous vous envoyons un lien de réinitialisation."
      footer={
        <>
          Vous vous en souvenez ? <Link to="/connexion">Se connecter</Link>
        </>
      }
    >
      {sent ? (
        <Alert type="success">
          Si un compte existe pour cette adresse, un email de réinitialisation
          vient d'être envoyé.
        </Alert>
      ) : (
        <form onSubmit={submit}>
          <Alert>{error}</Alert>
          <Field
            label="Adresse email"
            id="email"
            type="email"
            placeholder="vous@exemple.com"
            value={values.email}
            onChange={handleChange}
            required
          />
          <Button type="submit" block disabled={loading}>
            {loading ? "Envoi..." : "Envoyer le lien"}
          </Button>
        </form>
      )}
    </AuthLayout>
  );
}