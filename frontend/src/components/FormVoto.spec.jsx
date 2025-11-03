import { render, screen, fireEvent } from "@testing-library/react";
import FormVoto from "../components/FormVoto";
import api from "../api/api";

jest.mock("../api/api");

describe("FormVoto", () => {
  test("renderiza inputs e botão", () => {
    render(<FormVoto />);
    expect(screen.getByPlaceholderText("ID da Sessão")).toBeInTheDocument();
    expect(screen.getByText("Votar")).toBeInTheDocument();
  });

  test("chama a API ao registrar voto", async () => {
    api.post.mockResolvedValueOnce({});
    render(<FormVoto />);
    
    fireEvent.change(screen.getByPlaceholderText("ID da Sessão"), { target: { value: "1" } });
    fireEvent.change(screen.getByDisplayValue("SIM"), { target: { value: "NAO" } });
    fireEvent.click(screen.getByText("Votar"));

    expect(api.post).toHaveBeenCalledWith("/votos", { idSessao: "1", escolha: "NAO" });
  });
});
