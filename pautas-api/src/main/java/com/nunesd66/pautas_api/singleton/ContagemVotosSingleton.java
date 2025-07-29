package com.nunesd66.pautas_api.singleton;

import com.nunesd66.pautas_api.voto.model.Voto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ContagemVotosSingleton {

    private static  ContagemVotosSingleton contagemVotosSingleton;
    private final List<Voto> votos;

    private ContagemVotosSingleton() {
        this.votos = new ArrayList<>();
    }

    public static ContagemVotosSingleton getInstancia() {
        if (contagemVotosSingleton == null) {
            synchronized (ContagemVotosSingleton.class) {
                if (contagemVotosSingleton == null) {
                    contagemVotosSingleton = new ContagemVotosSingleton();
                }
            }
        }
        return contagemVotosSingleton;
    }

    public void adicionar(Voto voto) {
        this.votos.add(voto);
    }

    public boolean hasVotoByCpf(String cpfAssociado) {
        return this.votos.stream().anyMatch(voto -> voto.getCpfAssociado().equals(cpfAssociado));
    }

    public void resetarVotos() {
        this.votos.clear();
    }

}
