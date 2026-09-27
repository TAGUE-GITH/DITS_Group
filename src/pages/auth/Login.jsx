import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/authContext";
import useForm from "../../hooks/useForm";
import useSubmit from "../../hooks/useSubmit";
import AuthLayout from "../../components/auth/AuthLayout";
import Field from "../../components/ui/Field";
import PasswordInput from "../../components/ui/PasswordInput";
import Button from "../../components/ui/Button";
import Alert from "../../components/ui/Alert";
import usePageTitle from "../../hooks/usePageTitle";

export default function Login() {
  usePageTitle("Connexion", "Connectez-vous pour accéder à votre espace personnel.");

  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const { values, handleChange } = useForm({ email: "", password: "" });

  const { submit, loading, error } = useSubmit(async () => {
    const user = await login(values.email, values.password);
    const fallback = user.isAdmin ? "/admin" : "/mon-espace";
    navigate(location.state?.from || fallback, { replace: true });
  });

  return (
    <AuthLayout
      variant="login"
      visualTitle="Heureux de vous revoir"
      visualText="Connectez-vous pour accéder à votre espace personnel et suivre vos projets avec DITS Group."
      title="Connexion"
      subtitle="Entrez vos identifiants pour continuer."
      footer={
        <>
          Pas encore de compte ? <Link to="/inscription">Créer un compte</Link>
        </>
      }
    >
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
        <PasswordInput
          label="Mot de passe"
          id="password"
          placeholder="Votre mot de passe"
          value={values.password}
          onChange={handleChange}
          required
        />
        <p className="auth-forgot">
          <Link to="/mot-de-passe-oublie">Mot de passe oublié ?</Link>
        </p>
        <Button type="submit" block disabled={loading}>
          {loading ? "Connexion..." : "Se connecter"}
        </Button>
      </form>
    </AuthLayout>
  );
}