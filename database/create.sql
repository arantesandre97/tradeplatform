drop schema if exists ccca;

create schema tradeplatform;

create table tradeplatform.account (
    account_id uuid primary key,
    name text,
    email text,
    document text,
    password text,
    creation_date timestamptz,
    last_update_date timestamptz
);

create table tradeplatform.balance (
    account_id uuid references tradeplatform.account(account_id),
    asset_id text,
    amount numeric,
    blocked_amount numeric,
    last_update_date timestamptz,
    primary key (account_id, asset_id)
);

create table tradeplatform.order (
    order_id uuid primary key,
    account_id uuid references tradeplatform.account(account_id),
    market_id text,
    order_type text,
    quantity numeric,
    filled_quantity numeric,
    price numeric,
    order_status text,
    creation_date timestamptz,
    last_update_date timestamptz
);