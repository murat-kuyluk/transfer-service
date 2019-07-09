drop table if exists accounts;

create table accounts(

  id bigint not null AUTO_INCREMENT,
  owner_full_name varchar(128),
  account_number varchar(8) not null,
  sort_code varchar(8) not null,
  balance decimal(19,2) not null,
  create_date timestamp,
  modify_date timestamp,
  version int not null default 0,

  PRIMARY KEY (id)
);

drop table transfers if exists;

create table transfers (
    id bigint not null AUTO_INCREMENT,
    amount decimal(19,2) not null,
    reference varchar(64),
    from_account varchar(20) not null,
    to_account varchar(20) not null,

    primary key (id)
);