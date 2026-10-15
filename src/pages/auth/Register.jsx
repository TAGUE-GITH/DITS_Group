import { useState } from "react";
import { Link } from "react-router-dom";
import { register } from "../../api/authApi";
import useForm from "../../hooks/useForm";
import useSubmit from "../../hooks/useSubmit";
import AuthLayout from "../../components/auth/AuthLayout";
import Field from "../../components/ui/Field";
import PasswordInput from "../../components/ui/PasswordInput";
import Button from "../../components/ui/Button";
import Alert from "../../components/ui/Alert";
import usePageTitle from "../../hooks/usePageTitle";

export default function Register() {
  usePageTitle("Inscription", "Créez votre compte pour accéder à votre espace personnel.");

  const [done, setDone] = useState(false);
  const { values, handleChange } = useForm({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    password: "",
    confirmPassword: "",
  });

  const { submit, loading, error } = useSubmit(async () => {
    if (values.password.length < 8) {
      throw new Error("Le mot de passe doit contenir au moins 8 caractères.");
    }

    if (values.password !== values.confirmPassword) {
      throw new Error("Les mots de passe ne correspondent pas.");
    }

    const userData = { ...values };
    delete userData.confirmPassword;
    await register(userData);
    setDone(true);
  });

  return (
    <AuthLayout
      variant="register"
      visualTitle="Rejoignez DITS Group"
      visualText="Créez votre compte pour accéder à vos documents, suivre vos demandes et échanger avec nos équipes."
      title="Créer un compte"
      subtitle="Quelques informations suffisent pour commencer."
      footer={
        <>
          Déjà inscrit ? <Link to="/connexion">Se connecter</Link>
        </>
      }
    >
      {done ? (
        <>
          <Alert type="success">
            Votre compte a été créé. Il sera activé après validation par un
            administrateur.
          </Alert>
          <Button to="/connexion" block>
            Aller à la connexion
          </Button>
        </>
      ) : (
        <form onSubmit={submit}>
          <Alert>{error}</Alert>
          <div className="auth-row">
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
            placeholder="vous@exemple.com"
            value={values.email}
            onChange={handleChange}
            required
          />
          <Field
            label="Téléphone"
            id="phone"
            type="tel"
            placeholder="06 00 00 00 00"
            value={values.phone}
            onChange={handleChange}
          />
          <PasswordInput
            label="Mot de passe"
            id="password"
            placeholder="8 caractères minimum"
            value={values.password}
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
            {loading ? "Création..." : "Créer mon compte"}
          </Button>
        </form>
      )}
    </AuthLayout>
  );
}