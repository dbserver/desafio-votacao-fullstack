import { render, screen, fireEvent } from "@testing-library/react";
import FormPauta from "../components/FormPauta";
import api from "../api/api";

jest.mock("../api/api"); 

describe("FormPauta", () => {
  test("renderiza inputs e botão", () => {
    render(<FormPauta />);
    expect(screen.getByPlaceholderText("Título")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Descrição")).toBeInTheDocument();
    expect(screen.getByText("Criar")).toBeInTheDocument();
  });

  test("chama a API ao criar pauta", async () => {
    api.post.mockResolvedValueOnce({}); 
    render(<FormPauta />);
    
    fireEvent.change(screen.getByPlaceholderText("Título"), { target: { value: "Teste" } });
    fireEvent.change(screen.getByPlaceholderText("Descrição"), { target: { value: "Descrição teste" } });
    fireEvent.click(screen.getByText("Criar"));

    expect(api.post).toHaveBeenCalledWith("/pauta", { titulo: "Teste", descricao: "Descrição teste" });
  });
});
