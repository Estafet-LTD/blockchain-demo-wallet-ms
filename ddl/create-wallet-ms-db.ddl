create table MESSAGE_EVENT (TOPIC_ID varchar(255) not null, MESSAGE_REFERENCE varchar(255) not null, VERSION int4, primary key (TOPIC_ID));
create table WALLET (WALLET_ADDRESS varchar(255) not null, WALLET_NAME varchar(255) not null, primary key (WALLET_ADDRESS));
