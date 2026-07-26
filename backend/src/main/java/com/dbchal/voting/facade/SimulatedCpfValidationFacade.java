package com.dbchal.voting.facade;

import java.util.random.RandomGenerator;

import org.springframework.stereotype.Component;

import com.dbchal.voting.domain.enums.CpfValidationStatus;
import com.dbchal.voting.exception.associado.InvalidCpfException;

@Component
public class SimulatedCpfValidationFacade implements CpfValidationFacade {

	private final RandomGenerator randomGenerator;

	public SimulatedCpfValidationFacade() {
		this(RandomGenerator.getDefault());
	}

	SimulatedCpfValidationFacade(RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}

	@Override
	public CpfValidationStatus validate(String cpf) {
		if (!isValidCpf(cpf)) {
			throw new InvalidCpfException();
		}

		return randomGenerator.nextBoolean() ? CpfValidationStatus.ABLE_TO_VOTE : CpfValidationStatus.UNABLE_TO_VOTE;
	}

	private boolean isValidCpf(String cpf) {
		if (cpf == null || !cpf.matches("\\d{11}")) {
			return false;
		}

		if (cpf.chars().distinct().count() == 1) {
			return false;
		}

		int firstDigit = calculateDigit(cpf, 9, 10);
		int secondDigit = calculateDigit(cpf, 10, 11);

		return firstDigit == Character.digit(cpf.charAt(9), 10) && secondDigit == Character.digit(cpf.charAt(10), 10);
	}

	private int calculateDigit(String cpf, int length, int initialWeight) {

		int sum = 0;

		for (int index = 0; index < length; index++) {
			int digit = Character.digit(cpf.charAt(index), 10);

			sum += digit * (initialWeight - index);
		}

		int result = 11 - (sum % 11);

		return result >= 10 ? 0 : result;
	}
}