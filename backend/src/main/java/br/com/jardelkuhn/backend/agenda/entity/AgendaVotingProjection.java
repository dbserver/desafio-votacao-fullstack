package br.com.jardelkuhn.backend.agenda.entity;

import java.time.LocalDateTime;

public record AgendaVotingProjection(
        Long id,
        String title,
        String description,
        LocalDateTime openedDate,
        LocalDateTime limitDate,
        long yesVotes,
        long noVotes,
        long totalVotes
) {
    private static AgendaVotingProjection fromBuilder(Builder builder) {
        return new AgendaVotingProjection(
                builder.id,
                builder.title,
                builder.description,
                builder.openedDate,
                builder.limitDate,
                builder.yesVotes,
                builder.noVotes,
                builder.totalVotes
        );
    }

    public static class Builder {
        private long id;
        private String title;
        private String description;
        private LocalDateTime openedDate;
        private LocalDateTime limitDate;
        private long yesVotes;
        private long noVotes;
        private long totalVotes;

        public Builder(long id) {
            this.id = id;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setOpenedDate(LocalDateTime openedDate) {
            this.openedDate = openedDate;
            return this;
        }

        public Builder setLimitDate(LocalDateTime limitDate) {
            this.limitDate = limitDate;
            return this;
        }

        public Builder setNoVotes(long noVotes) {
            this.noVotes = noVotes;
            return this;
        }

        public Builder setYesVotes(long yesVotes) {
            this.yesVotes = yesVotes;
            return this;
        }

        public Builder setTotalVotes(long totalVotes) {
            this.totalVotes = totalVotes;
            return this;
        }

        public AgendaVotingProjection build() {
            return AgendaVotingProjection.fromBuilder(this);
        }
    }
}