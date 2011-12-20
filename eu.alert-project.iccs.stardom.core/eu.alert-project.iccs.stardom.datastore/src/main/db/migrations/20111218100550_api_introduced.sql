create table path_signature_history(
  id int primary key not null unique,
  path text,
  signature text,
  identity_id int not null,
  FOREIGN KEY (identity_id) REFERENCES `identity`(id) ON DELETE CASCADE ON UPDATE CASCADE
)engine=innodb;