package br.dev.ederson.spring.cooperativa.enumerador;

public enum AgendaStatus {
    TO_VOTE(0),
    VOTING(1),
    APPROVED(3),
    REJECTED(4);

    private int value;

    AgendaStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
