package com.db.votacao.api.dto;

import com.db.votacao.api.model.enums.VoteAbilityStatusEnum;

public class CpfValidationResponse {
    private VoteAbilityStatusEnum status;


    public CpfValidationResponse(VoteAbilityStatusEnum status) {
        this.status = status;
    }

    public VoteAbilityStatusEnum getStatus() {
        return status;
    }

    public void setStatus(VoteAbilityStatusEnum status) {
        this.status = status;
    }
}
