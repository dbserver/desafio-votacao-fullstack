package com.sousadev.voteapp.dtos;

import com.sousadev.voteapp.entity.Pauta;
import com.sousadev.voteapp.entity.Vote;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record PautaResponseDto(
        UUID id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime expireIn,
        List<VoteResponseDto> votes) {

    public static PautaResponseDto fromEntity(Pauta pauta) {
        List<VoteResponseDto> voteDtos = (pauta.getVotes() == null) ? List.of()
                : pauta.getVotes().stream()
                        .map(VoteResponseDto::fromEntity)
                        .toList();
        return PautaResponseDto.builder()
                .id(pauta.getId())
                .name(pauta.getName())
                .description(pauta.getDescription())
                .createdAt(pauta.getCreatedAt())
                .expireIn(pauta.getExpireIn())
                .votes(voteDtos)
                .build();
    }

    public static record VoteResponseDto(
            UUID id,
            UUID associateId,
            String voteValue) {
        public static VoteResponseDto fromEntity(Vote vote) {
            return new VoteResponseDto(
                    vote.getId(),
                    vote.getAssociate().getId(),
                    vote.getValue().name()
            );
        }
    }
}
