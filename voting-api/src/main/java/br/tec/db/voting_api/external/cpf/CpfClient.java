package br.tec.db.voting_api.external.cpf;

public interface CpfClient {
    CpfResponse validateCPF(String cpf);
}
