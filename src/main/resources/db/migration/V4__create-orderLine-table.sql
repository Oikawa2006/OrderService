create table if not exists orderLine_table
(
    id bigserial primary key,
    order_id bigint references order_table(id),
    goods_id bigint references goods_table(id),
    count bigint);
