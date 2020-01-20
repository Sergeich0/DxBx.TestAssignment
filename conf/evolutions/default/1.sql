# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table guest (
  id                            bigserial not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  phone                         varchar(255),
  email                         varchar(255),
  active                        boolean default false not null,
  constraint uq_guest_phone unique (phone),
  constraint uq_guest_email unique (email),
  constraint pk_guest primary key (id)
);

create table guest_restaurant (
  guest_id                      bigint not null,
  restaurant_id                 bigint not null,
  constraint pk_guest_restaurant primary key (guest_id,restaurant_id)
);

create table restaurant (
  id                            bigserial not null,
  inn                           varchar(255),
  name                          varchar(255),
  ulname                        varchar(255),
  address                       varchar(255),
  active                        boolean default false not null,
  constraint uq_restaurant_inn unique (inn),
  constraint pk_restaurant primary key (id)
);

create index ix_guest_restaurant_guest on guest_restaurant (guest_id);
alter table guest_restaurant add constraint fk_guest_restaurant_guest foreign key (guest_id) references guest (id) on delete restrict on update restrict;

create index ix_guest_restaurant_restaurant on guest_restaurant (restaurant_id);
alter table guest_restaurant add constraint fk_guest_restaurant_restaurant foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;


# --- !Downs

alter table if exists guest_restaurant drop constraint if exists fk_guest_restaurant_guest;
drop index if exists ix_guest_restaurant_guest;

alter table if exists guest_restaurant drop constraint if exists fk_guest_restaurant_restaurant;
drop index if exists ix_guest_restaurant_restaurant;

drop table if exists guest cascade;

drop table if exists guest_restaurant cascade;

drop table if exists restaurant cascade;

