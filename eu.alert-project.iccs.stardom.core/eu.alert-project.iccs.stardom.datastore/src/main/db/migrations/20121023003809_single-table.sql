create table metric_single_table(
  id int primary key not null unique,
  identity_id int not null,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  temporal timestamp not null,
  `quantity` integer not null default 0,
   message_id VARCHAR(255) NULL,
   in_reply_to VARCHAR(255) NULL,
   amount integer not null default 0,
   component VARCHAR(255) NULL,
   type varchar(255) NOT NULL,
  FOREIGN KEY (identity_id) REFERENCES `identity`(id) ON DELETE CASCADE ON UPDATE CASCADE
)engine=innodb;

insert into sequence values('metric_single_sequence',0);