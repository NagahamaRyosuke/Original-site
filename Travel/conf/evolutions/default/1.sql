# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table distance (
  distance_id               bigint auto_increment not null,
  distance_title            varchar(255),
  userId                    bigint,
  constraint pk_distance primary key (distance_id))
;

create table travel_box (
  id                        bigint auto_increment not null,
  date                      varchar(255),
  start_time                varchar(255),
  end_time                  varchar(255),
  title                     varchar(255),
  place                     varchar(255),
  distance_id               bigint,
  constraint pk_travel_box primary key (id))
;

create table user (
  user_id                   bigint auto_increment not null,
  user_name                 varchar(255),
  password                  varchar(255),
  mail                      varchar(255),
  create_date               datetime not null,
  constraint pk_user primary key (user_id))
;

alter table distance add constraint fk_distance_user_1 foreign key (userId) references user (user_id) on delete restrict on update restrict;
create index ix_distance_user_1 on distance (userId);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table distance;

drop table travel_box;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

