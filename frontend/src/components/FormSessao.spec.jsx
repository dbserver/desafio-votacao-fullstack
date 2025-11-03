import { render, screen, fireEvent } from "@testing-library/react";
import FormSessao from "../components/FormSessao";
import api from "../api/api";

jest.mock("../api/api");

describe("FormSessao", () => {
  test("renderiza inputs e botão", () => {
    render(<FormSessao />);
    expect(screen.getByPlaceholderText("ID da Pauta")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Início")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Fim")).toBeInTheDocument();
    expect(screen.getByText("Abrir")).toBeInTheDocument();
  });

  test("chama a API ao abrir sessão", async () => {
    api.post.mockResolvedValueOnce({});
    render(<FormSessao />);

    fireEvent.change(screen.getByPlaceholderText("ID da Pauta"), { target: { value: "1" } });
    fireEvent.change(screen.getByPlaceholderText("Início"), { target: { value: "2025-11-02T10:00" } });
    fireEvent.change(screen.getByPlaceholderText("Fim"), { target: { value: "2025-11-02T12:00" } });
    fireEvent.click(screen.getByText("Abrir"));

    expect(api.post).toHaveBeenCalledWith("/sessao", {
      idPauta: "1",
      inicioSessao: "2025-11-02T10:00",
      fimSessao: "2025-11-02T12:00"
    });
  });
});
