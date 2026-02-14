drop schema if exists ccca;

create schema ccca;

create table ccca.account (
    account_id uuid primary key,
    name text,
    email text,
    document text,
    password text
);

creata table ccca.balance (
    account_id uuid references ccca.account(account_id),
    asset_id text,
    amount bigint,
    last_update_date timestamp
    primary key (account_id, asset_id)
);