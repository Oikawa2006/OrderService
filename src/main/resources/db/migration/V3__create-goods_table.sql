create table if not exists goods_table(
    id bigserial primary key,
    name varchar(255) not null ,
    price bigint not null
)