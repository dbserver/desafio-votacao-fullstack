package com.desafio.votacao_api.model;

import com.desafio.votacao_api.exception.InvalidCpfException;

import java.util.Objects;

public class Cpf {

    private String number;

    public Cpf(String number) {
        if(number == null || number.length() != 11) {
            throw new InvalidCpfException("CPF must be 11 digits long");
        }
        this.number = number;
    }

    public String getNumber() {
        return this.number;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cpf cpf = (Cpf) o;
        return Objects.equals(number, cpf.number);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }
}
