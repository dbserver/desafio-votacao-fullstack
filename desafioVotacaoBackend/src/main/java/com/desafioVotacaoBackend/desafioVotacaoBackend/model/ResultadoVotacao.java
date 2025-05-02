package com.desafioVotacaoBackend.desafioVotacaoBackend.model;

public class ResultadoVotacao {
    private final long votosSim;
    private final long votosNao;

    public ResultadoVotacao(long votosSim, long votosNao) {
        this.votosSim = votosSim;
        this.votosNao = votosNao;
    }

    public long getVotosSim() {
        return votosSim;
    }

    public long getVotosNao() {
        return votosNao;
    }

    public boolean isAprovada() {
        return votosSim > votosNao;
    }

    @Override
    public String toString() {
        return "ResultadoVotacao{" +
                "votosSim=" + votosSim +
                ", votosNao=" + votosNao +
                '}';
    }
}
