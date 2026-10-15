import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { resetPassword } from "../../api/authApi";
import useForm from "../../hooks/useForm";
import useSubmit from "../../hooks/useSubmit";
import AuthLayout from "../../components/auth/AuthLayout";
import PasswordInput from "../../components/ui/PasswordInput";
import Button from "../../components/ui/Button";
import Alert from "../../components/ui/Alert";

export default function ResetPassword() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");
  const [done, setDone] = useState(false);
  const { values, handleChange } = useForm({
    newPassword: "",
    confirmPassword: "",
  });

  const { submit, loading, error } = useSubmit(async () => {
    if (values.newPassword.length < 8) {
      throw new Error("Le mot de passe doit contenir au moins 8 caractères.");
    }

    if (values.newPassword !== values.confirmPassword) {
      throw new Error("Les mots de passe ne correspondent pas.");
    }

    await resetPassword(token, values.newPassword);
    setDone(true);
  });

  return (
    <AuthLayout
      variant="lock"
      visualTitle="Un nouveau départ sécurisé"
      visualText="Choisissez un mot de passe robuste pour protéger votre espace personnel."
      title="Nouveau mot de passe"
      subtitle="Définissez votre nouveau mot de passe."
      footer={<Link to="/connexion">Retour à la connexion</Link>}
    >
      {!token ? (
        <Alert>Lien invalide ou expiré. Refaites une demande de réinitialisation.</Alert>
      ) : done ? (
        <>
          <Alert type="success">Votre mot de passe a été modifié.</Alert>
          <Button to="/connexion" block>
            Se connecter
          </Button>
        </>
      ) : (
        <form onSubmit={submit}>
          <Alert>{error}</Alert>
          <PasswordInput
            label="Nouveau mot de passe"
            id="newPassword"
            placeholder="8 caractères minimum"
            value={values.newPassword}
            onChange={handleChange}
            required
          />
          <PasswordInput
            label="Confirmer le mot de passe"
            id="confirmPassword"
            value={values.confirmPassword}
            onChange={handleChange}
            required
          />
          <Button type="submit" block disabled={loading}>
            {loading ? "Enregistrement..." : "Modifier le mot de passe"}
          </Button>
        </form>
      )}
    </AuthLayout>
  );
}