import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import ResultadoPage from "../pages/ResultadoPage";
import api from "../api/api";

jest.mock("../api/api");

describe("ResultadoPage", () => {
  test("renderiza input e botão", () => {
    render(<ResultadoPage />);
    expect(screen.getByPlaceholderText("ID da Sessão")).toBeInTheDocument();
    expect(screen.getByText("Buscar")).toBeInTheDocument();
  });

  test("busca resultado via API", async () => {
    api.get.mockResolvedValueOnce({ data: { votosSim: 10, votosNao: 5 } });
    render(<ResultadoPage />);

    fireEvent.change(screen.getByPlaceholderText("ID da Sessão"), { target: { value: "1" } });
    fireEvent.click(screen.getByText("Buscar"));

    await waitFor(() => {
      expect(screen.getByText("Votos SIM: 10")).toBeInTheDocument();
      expect(screen.getByText("Votos NÃO: 5")).toBeInTheDocument();
    });
  });
});
