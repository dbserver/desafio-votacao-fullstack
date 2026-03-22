package com.desafio.votacao_api.model;

import com.desafio.votacao_api.exception.InvalidCpfException;

import java.util.Objects;

/**
 * Value object that represents a Brazilian CPF number.
 */
public class Cpf {

    /**
     * CPF number in a numeric 11-digit format.
     */
    private String number;

    /**
     * Creates a CPF value object.
     *
     * @param number CPF number with exactly 11 digits
     * @throws InvalidCpfException if number is null or does not have 11 characters
     */
    public Cpf(String number) {
        if(number == null || number.length() != 11) {
            throw new InvalidCpfException("CPF must be 11 digits long");
        }
        this.number = number;
    }

    /**
     * Returns the CPF number.
     *
     * @return CPF number in string format
     */
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
