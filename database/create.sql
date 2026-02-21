drop schema if exists ccca;

create schema ccca;

create table account (
    account_id uuid primary key,
    name text,
    email text,
    document text,
    password text
);

create table balance (
    account_id uuid references account(account_id),
    asset_id text,
    amount bigint,
    last_update_date timestamp,
    primary key (account_id, asset_id)
);