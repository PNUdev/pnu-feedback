create table educational_program(
  id                       bigserial    primary key,
  title                    varchar(255) not null
);

create table stakeholder_category(
  id                       bigserial    primary key,
  title                    varchar(255) not null
);

create table submission(
  id                       bigserial    primary key,
  educational_program_id   bigint       not null,
  stakeholder_category_id  bigint       not null,
  submission_time          timestamp,

  constraint fk_educational_program foreign key (educational_program_id) references educational_program(id),
  constraint fk_stakeholder_category foreign key (stakeholder_category_id) references stakeholder_category(id)
);

create table open_answer(
  id                       bigserial    primary key,
  submission_id            bigint       not null,
  content                  text not null,
  approved   boolean       not null,

  constraint fk_submission foreign key (submission_id) references submission(id)
);

create table score_answer(
  id                       bigserial    primary key,
  question_number          varchar(10) not null,
  submission_id            bigint       not null,
  score                    int          not null,

  constraint fk_submission foreign key (submission_id) references submission(id)
);

create table score_question(
  id                       bigserial    primary key,
  question_number          varchar(10) not null,
  stakeholder_category_id  bigint       not null,
  content                  text not null,

  constraint fk_stakeholder_category foreign key (stakeholder_category_id) references stakeholder_category(id)
);

