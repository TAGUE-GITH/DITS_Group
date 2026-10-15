import { render, screen } from "@testing-library/react";
import useFetch from "../hooks/useFetch";

function List({ fetcher }) {
  const { data, loading, error } = useFetch(fetcher);
  if (loading) return <p>Chargement</p>;
  if (error) return <p>Erreur : {error}</p>;
  return <p>{data.join(", ")}</p>;
}

describe("useFetch", () => {
  it("charge les donnees", async () => {
    const fetcher = () => Promise.resolve(["Audit", "Cloud"]);
    render(<List fetcher={fetcher} />);
    expect(await screen.findByText("Audit, Cloud")).toBeInTheDocument();
  });

  it("affiche le message d'erreur", async () => {
    const fetcher = () => Promise.reject(new Error("Serveur indisponible"));
    render(<List fetcher={fetcher} />);
    expect(await screen.findByText("Erreur : Serveur indisponible")).toBeInTheDocument();
  });
});
