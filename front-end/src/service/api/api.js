import axios from "axios";

class ApiService {
  constructor(baseURL) {
    this.api = axios.create({
      baseURL: baseURL,
    });
  }

  // Método para obter todas as pautas
  getAllDiscusses(page,searchStatus) {
    return this.api.get(`/api/discuss?page=${page}&searchStatus=${searchStatus}`);
  }

  // Método para adicionar uma nova pauta
  addDiscuss(discuss) {
    return this.api.post("/api/discuss", discuss, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  }

  // Método para atualizar uma pauta existente
  updateDiscuss(id, discuss) {
    return this.api.put(`/api/discuss/${id}`, discuss);
  }

  // Método para deletar uma pauta
  deleteDiscuss(id) {
    return this.api.delete(`/api/discuss/${id}`);
  }

  // Método para votar
  addVote(vote) {
    return this.api.post(`/api/vote`, vote, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  }

  registerAssociate(associate) {
    return this.api.post(`/api/associated`, associate, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  }
  

}

const apiService = new ApiService("http://localhost:8080");

export default apiService;
