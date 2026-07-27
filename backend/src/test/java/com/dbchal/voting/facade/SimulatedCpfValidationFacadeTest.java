package com.dbchal.voting.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dbchal.voting.domain.enums.CpfValidationStatus;
import com.dbchal.voting.exception.associado.InvalidCpfException;

@ExtendWith(MockitoExtension.class)
class SimulatedCpfValidationFacadeTest {

	@Mock
	private RandomGenerator randomGenerator;

	@Test
	void shouldReturnAbleToVote() {
		SimulatedCpfValidationFacade facade = new SimulatedCpfValidationFacade(randomGenerator);

		when(randomGenerator.nextBoolean()).thenReturn(true);

		CpfValidationStatus resultado = facade.validate("52998224725");

		assertEquals(CpfValidationStatus.ABLE_TO_VOTE, resultado);
	}

	@Test
	void shouldReturnUnableToVote() {
		SimulatedCpfValidationFacade facade = new SimulatedCpfValidationFacade(randomGenerator);

		when(randomGenerator.nextBoolean()).thenReturn(false);

		CpfValidationStatus resultado = facade.validate("52998224725");

		assertEquals(CpfValidationStatus.UNABLE_TO_VOTE, resultado);
	}

	@Test
	void shouldThrowExceptionWhenCpfIsInvalid() {
		SimulatedCpfValidationFacade facade = new SimulatedCpfValidationFacade(randomGenerator);

		assertThrows(InvalidCpfException.class, () -> facade.validate("11111111111"));

		verifyNoInteractions(randomGenerator);
	}
}