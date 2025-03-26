create table if not exists order_table(
    id bigserial primary key not null ,
    client varchar(255) not null ,
    date date not null ,
    address varchar(128) not null
)