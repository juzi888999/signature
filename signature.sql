create schema if not exists signature collate utf8mb4_general_ci;

use signature;

create table if not exists apple
(
	id int auto_increment
		primary key,
	account varchar(255) null comment 'apple开发者帐号',
	count int not null comment '已有设备数量',
	p8 text not null comment '私钥',
	iss text not null comment '在Store Connect上可以点击复制 iss ID',
	kid text not null comment 'your own key ID',
	p12 varchar(255) null comment 'p12文件地址',
	cerId varchar(255) not null comment '授权证书id',
	bundleIds varchar(255) not null comment '开发者后台的通配证书id',
	create_time timestamp default CURRENT_TIMESTAMP not null comment '帐号添加时间'
)
comment '帐号';

create table if not exists device
(
	id int auto_increment
		primary key,
	udid varchar(255) not null comment '设备UDID',
	apple_id int not null comment '此设备所使用的帐号id',
	package_ids varchar(255) null comment '此设备所使用的安装包ids',
	device_id varchar(255) not null comment '设备id',
	creat_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
	constraint device_apple_id_fk
		foreign key (apple_id) references apple (id)
)
comment '设备';

create table if not exists package
(
	id int auto_increment
		primary key,
	name varchar(30) not null comment '包名',
	icon varchar(255) null comment '图标',
	version varchar(30) null comment '版本',
	build_version varchar(30) not null comment '编译版本号',
	mini_version varchar(30) not null comment '最小支持版本',
	bundle_identifier varchar(255) not null comment '安装包id',
	link varchar(255) not null comment '下载地址',
	mobileconfig varchar(255) null comment '获取UDID证书名称',
	summary text null comment '简介',
	create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
	count int default 0 not null comment '总下载量',
	constraint package_bundle_identifier_uindex
		unique (bundle_identifier),
	constraint package_url_uindex
		unique (link)
)
comment '安装包';

