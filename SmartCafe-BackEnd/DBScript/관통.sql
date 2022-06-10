drop database if exists ssafy_mobile_cafe;
-- select @@global.transaction_isolation, @@transaction_isolation;
-- set @@transaction_isolation="read-committed";

create database ssafy_mobile_cafe;
use ssafy_mobile_cafe;


create table t_user(
	id varchar(100) primary key,
    name varchar(100) not null,
    pass varchar(100) not null,
    stamps integer default 0
);
create table t_product(
	id integer auto_increment primary key,
    name varchar(100) not null,
    type varchar(20) not null,
    price integer not null,
    img varchar(100) not null
);


create  table t_order(
	o_id integer auto_increment primary key,
    user_id varchar(100) not null,
    order_table varchar(20),
    order_time timestamp default CURRENT_TIMESTAMP,    
    completed char(1) default 'N',
    constraint fk_order_user foreign key (user_id) references t_user(id) on delete cascade
);

create table t_order_detail(
	d_id integer auto_increment primary key,
    order_id integer not null,
    product_id integer not null,
    quantity integer not null default 1,
    constraint fk_order_detail_product foreign key (product_id) references t_product(id) on delete cascade,
    constraint fk_order_detail_order foreign key(order_id) references t_order(o_id) on delete cascade
);                                                 

create table t_stamp(
	id integer auto_increment primary key,
    user_id varchar(100) not null,
    order_id integer not null,
    quantity integer not null default 1,
    constraint fk_stamp_user foreign key (user_id) references t_user(id) on delete cascade,
    constraint fk_stamp_order foreign key (order_id) references t_order(o_id) on delete cascade
);

create table t_comment(
	id integer auto_increment primary key,
    user_id varchar(100) not null,
    product_id integer not null,
    rating float not null default 1,
    comment varchar(200),
    constraint fk_comment_user foreign key(user_id) references t_user(id) on delete cascade,
    constraint fk_comment_product foreign key(product_id) references t_product(id) on delete cascade
);

create table t_event(
	id integer auto_increment primary key,
    title varchar(100) not null,
    img varchar(100) not null
);

create table t_notification(
	id integer auto_increment primary key,
    user_id varchar(100) not null,
    content varchar(300) not null,
    time timestamp default CURRENT_TIMESTAMP
);

create table t_likemenu(
	id integer auto_increment primary key,
    user_id varchar(100) not null,
    product_id integer not null
);

INSERT INTO t_user (id, name, pass, stamps) VALUES ('ssafy01', '김싸피', 'pass01', 5);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('ssafy02', '황원태', 'pass02', 0);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('ssafy03', '한정일', 'pass03', 3);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('ssafy04', '반장운', 'pass04', 4);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('ssafy05', '박하윤', 'pass05', 5);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('ssafy06', '정비선', 'pass06', 6);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('ssafy07', '김병관', 'pass07', 7);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('ssafy08', '강석우', 'pass08', 8);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('ssafy09', '견본무', 'pass09', 9);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('ssafy10', '전인성', 'pass10', 20);

INSERT INTO t_product (name, type, price, img) VALUES ('아메리카노', 'coffee', 4100, 'm_americano.png');
INSERT INTO t_product (name, type, price, img) VALUES ('카페라떼', 'coffee', 4500, 'm_cafe_latte.png');
INSERT INTO t_product (name, type, price, img) VALUES ('카푸치노', 'coffee', 4800, 'm_capuchino.png');
INSERT INTO t_product (name, type, price, img) VALUES ('자몽에이드', 'coffee', 4800, 'm_jamong_ade.png');
INSERT INTO t_product (name, type, price, img) VALUES ('민트초코라떼', 'coffee', 4800, 'm_mint_choco_latte.png');
INSERT INTO t_product (name, type, price, img) VALUES ('모카라떼', 'coffee', 4300, 'm_mocalatte.png');
INSERT INTO t_product (name, type, price, img) VALUES ('레몬에이드', 'coffee', 4800, 'm_remon_ade.png');
INSERT INTO t_product (name, type, price, img) VALUES ('화이트모카라떼', 'coffee', 5100, 'm_white_moca_latte.png');
INSERT INTO t_product (name, type, price, img) VALUES ('초코케이크', 'desert', 6000, 'd_choco_cake.png');
INSERT INTO t_product (name, type, price, img) VALUES ('초코칩쿠키', 'desert', 4000, 'd_choco_chip.png');
INSERT INTO t_product (name, type, price, img) VALUES ('다크초코칩쿠키', 'desert', 4100, 'd_dark_choco_chip.png');
INSERT INTO t_product (name, type, price, img) VALUES ('망고케이크', 'desert', 4500, 'd_mango_cake.png');
INSERT INTO t_product (name, type, price, img) VALUES ('오레오케이크', 'desert', 5000, 'd_oreo_cake.png');
INSERT INTO t_product (name, type, price, img) VALUES ('레드벨벳케이크', 'desert', 5500, 'd_redvelvet_cake.png');
INSERT INTO t_product (name, type, price, img) VALUES ('티라미수', 'desert', 6000, 'd_tiramisu.png');


commit;
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy01', 1, 1, '신맛 강한 커피는 좀 별루네요.');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy02', 1, 2, '커피 맛을 좀 신경 써야 할 것 같네요.');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy03', 1, 3, '그냥 저냥');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy04', 4, 4, '갠춘한 맛');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy05', 5, 5, 'SoSSo');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy06', 6, 6, '그냥 저냥 먹을만함');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy07', 7, 10, '이집 화이트 모카라떼가 젤 나은듯');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy08', 8, 8, '자몽 특유의 맛이 살아있네요.');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy09', 8, 9, '수제 자몽에이드라 그런지 맛나요.');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy10', 10, 10, '초코칩 쿠키 먹으로 여기 옵니다.');

INSERT INTO t_order (user_id, order_table) VALUES ('ssafy01', 'order_table 01');
INSERT INTO t_order (user_id, order_table) VALUES ('ssafy01', 'order_table 02');
INSERT INTO t_order (user_id, order_table) VALUES ('ssafy03', 'order_table 03');
INSERT INTO t_order (user_id, order_table) VALUES ('ssafy04', 'order_table 04');
INSERT INTO t_order (user_id, order_table) VALUES ('ssafy05', 'order_table 05');
INSERT INTO t_order (user_id, order_table) VALUES ('ssafy06', 'order_table 06');
INSERT INTO t_order (user_id, order_table) VALUES ('ssafy07', 'order_table 07');
INSERT INTO t_order (user_id, order_table) VALUES ('ssafy08', 'order_table 08');
INSERT INTO t_order (user_id, order_table) VALUES ('ssafy09', 'order_table 09');
INSERT INTO t_order (user_id, order_table) VALUES ('ssafy10', 'order_table 10');

INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (1, 1, 1);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (1, 2, 3);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (2, 8, 1);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (3, 3, 3);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (4, 4, 4);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (5, 5, 5);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (6, 6, 6);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (7, 7, 7);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (8, 8, 8);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (9, 9, 9);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (10, 8, 10);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (10, 10, 10);


INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('ssafy01', 1, 4);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('ssafy01', 2, 1);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('ssafy03', 3, 3);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('ssafy04', 4, 4);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('ssafy05', 5, 5);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('ssafy06', 6, 6);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('ssafy07', 7, 7);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('ssafy08', 8, 8);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('ssafy09', 9, 9);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('ssafy10', 10, 20);

INSERT INTO t_event (title, img) VALUES ("summer1","event1.png");
INSERT INTO t_event (title, img) VALUES ("summer2","event2.png");
INSERT INTO t_event (title, img) VALUES ("summer3","event3.png");

INSERT INTO t_notification (user_id, content) VALUES ("ssafy01","주문이 완료되었습니다.");
INSERT INTO t_notification (user_id, content) VALUES ("ssafy01","주문하신 음식이 준비되었습니다.");
INSERT INTO t_notification (user_id, content) VALUES ("ssafy02","주문이 완료되었습니다.");
INSERT INTO t_notification (user_id, content) VALUES ("ssafy02","주문하신 음식이 준비되었습니다.");

INSERT INTO t_likemenu (user_id, product_id) VALUES ("ssafy01", 1);
INSERT INTO t_likemenu (user_id, product_id) VALUES ("ssafy01", 2);
INSERT INTO t_likemenu (user_id, product_id) VALUES ("ssafy01", 3);
INSERT INTO t_likemenu (user_id, product_id) VALUES ("ssafy02", 4);
INSERT INTO t_likemenu (user_id, product_id) VALUES ("ssafy02", 5);
INSERT INTO t_likemenu (user_id, product_id) VALUES ("ssafy02", 6);
INSERT INTO t_likemenu (user_id, product_id) VALUES ("ssafy03", 7);
INSERT INTO t_likemenu (user_id, product_id) VALUES ("ssafy03", 2);
INSERT INTO t_likemenu (user_id, product_id) VALUES ("ssafy04", 11);
INSERT INTO t_likemenu (user_id, product_id) VALUES ("ssafy04", 10);

commit;