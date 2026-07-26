package com.dbchal.voting.facade;

import com.dbchal.voting.domain.enums.CpfValidationStatus;

public interface CpfValidationFacade {

	CpfValidationStatus validate(String cpf);
}