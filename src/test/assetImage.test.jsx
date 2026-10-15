import { fireEvent, render, screen } from "@testing-library/react";
import AssetImage from "../components/ui/AssetImage";

describe("AssetImage", () => {
  it("affiche l'image envoyee par l'administration", () => {
    render(<AssetImage src="/uploads/services/a.jpg" alt="Audit" fallback={<span>DITS</span>} />);
    expect(screen.getByRole("img", { name: "Audit" })).toHaveAttribute("src", "http://localhost:8080/uploads/services/a.jpg");
  });

  it("affiche le visuel de remplacement si l'image est introuvable", () => {
    render(<AssetImage src="/uploads/services/perdue.jpg" alt="Audit" fallback={<span>DITS</span>} />);
    fireEvent.error(screen.getByRole("img", { name: "Audit" }));
    expect(screen.queryByRole("img")).not.toBeInTheDocument();
    expect(screen.getByText("DITS")).toBeInTheDocument();
  });

  it("affiche directement le visuel de remplacement sans image", () => {
    render(<AssetImage src="" alt="Audit" fallback={<span>DITS</span>} />);
    expect(screen.getByText("DITS")).toBeInTheDocument();
  });
});
