-- Таблица с постами
create table if not exists posts(
  id bigserial primary key,
  title varchar(256) not null,
  text text not null,
  likesCount integer not null,
  commentsCount integer not null);

-- Таблица с постами
create table if not exists tags(
  id bigserial primary key,
  tag varchar(256) not null,
  post_id bigserial NOT NULL,
  foreign key (post_id) references posts(id) on delete cascade);

---------------------------------

insert into posts(title, text, likesCount, commentsCount) values ('Название поста 1', 'Текст поста в формате Markdown...', 5, 1);
insert into posts(title, text, likesCount, commentsCount) values ('Название поста 2', 'Текст поста в формате Markdown...', 1, 5);

insert into tags(tag, post_id) values ('tag_1', 1);
insert into tags(tag, post_id) values ('tag_2', 1);