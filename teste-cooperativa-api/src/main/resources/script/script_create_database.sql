create sequence associate_seq;

alter sequence associate_seq owner to postgres;

create sequence agenda_seq;

alter sequence agenda_seq owner to postgres;

create sequence session_seq;

alter sequence session_seq owner to postgres;

create sequence vote_seq;

alter sequence vote_seq owner to postgres;

create table if not exists associate
(
    id       bigint not null
    constraint associate_pk
    primary key,
    name     varchar(100),
    document varchar(14)
    );

alter table associate
    owner to postgres;

create table if not exists session
(
    id            bigint            not null
    constraint session_pk
    primary key,
    duration_time integer default 1 not null,
    start_date    timestamp,
    end_date      timestamp
);

alter table session
    owner to postgres;

create table if not exists agenda
(
    description       text     not null,
    status            smallint not null,
    id                bigint   not null
    constraint agenda_pk
    primary key,
    registration_date timestamp,
    result_date       timestamp,
    session_id        bigint
    constraint agenda_session_fk
    references session
);

alter table agenda
    owner to postgres;

create table if not exists vote
(
    associate_id bigint
    constraint associate_agenda_associate_fk
    references associate,
    agenda_id    bigint
    constraint associate_agenda_agenda_fk
    references agenda,
    vote_date    timestamp,
    id           bigint not null
    constraint associate_agenda_pk
    primary key,
    approve      boolean
);

alter table vote
    owner to postgres;

