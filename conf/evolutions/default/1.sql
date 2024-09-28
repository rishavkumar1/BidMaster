# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table auction (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  vendor_id                     bigint,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint pk_auction primary key (id)
);

create table auction_product (
  id                            bigint auto_increment not null,
  auction_id                    bigint,
  product_id                    bigint,
  base_price                    double,
  status                        varchar(7),
  bid_start_time                datetime(6),
  bid_end_time                  datetime(6),
  current_highest_bid           double,
  highest_bid_id                bigint,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint ck_auction_product_status check (status in ('PENDING','RUNNING','CLOSED')),
  constraint pk_auction_product primary key (id)
);

create table bid (
  id                            bigint auto_increment not null,
  auction_product_id            bigint,
  bidder_id                     bigint,
  bid_timestamp                 datetime(6),
  price                         double,
  status                        varchar(9),
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint ck_bid_status check (status in ('EXPIRED','RUNNING','CONFIRMED')),
  constraint pk_bid primary key (id)
);

create table product (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  description                   varchar(255),
  category                      varchar(8),
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint ck_product_category check (category in ('clothing','watches','bike','mobile','car')),
  constraint pk_product primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint pk_user primary key (id)
);


# --- !Downs

drop table if exists auction;

drop table if exists auction_product;

drop table if exists bid;

drop table if exists product;

drop table if exists user;

