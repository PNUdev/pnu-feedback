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
  submission_time          timestamp
);

create table open_answer(
  id                       bigserial    primary key,
  educational_program_id   bigint       not null,
  stakeholder_category_id  bigint       not null,
  submission_id            bigint       not null,
  content                  varchar(255) not null,
  approved   bit           not null,

  constraint fk_educational_program foreign key (educational_program_id) references educational_program(id),
  constraint fk_stakeholder_category foreign key (stakeholder_category_id) references stakeholder_category(id),
  constraint fk_submission foreign key (submission_id) references submission(id)
);

create table score_answer(
  id                       bigserial    primary key,
  educational_program_id   bigint       not null,
  stakeholder_category_id  bigint       not null,
  submission_id            bigint       not null,
  score                    int          not null,

  constraint fk_educational_program foreign key (educational_program_id) references educational_program(id),
  constraint fk_stakeholder_category foreign key (stakeholder_category_id) references stakeholder_category(id),
  constraint fk_submission foreign key (submission_id) references submission(id)
);

