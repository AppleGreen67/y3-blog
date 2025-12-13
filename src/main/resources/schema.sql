-- Таблица с постами
create table if not exists posts(
  id bigserial primary key,
  title varchar(256) not null,
  text text not null,
  likesCount integer not null);

-- Таблица с тэгами
create table if not exists tags(
  id bigserial primary key,
  tag varchar(256) not null,
  post_id bigserial not null,
  foreign key (post_id) references posts(id) on delete cascade);

-- Таблица с комментариями
create table if not exists comments(
  id bigserial primary key,
  text text not null,
  post_id bigserial not null,
  foreign key (post_id) references posts(id) on delete cascade);

-- Таблица с картинками
create table if not exists images(
  name text not null,
  data blob not null,
  post_id bigserial primary key,
  foreign key (post_id) references posts(id) on delete cascade);

---------------------------------

--insert into posts(title, text, likesCount) values ('Название поста 1', 'Текст поста в формате Markdown...', 5);
--insert into posts(title, text, likesCount) values ('Название поста 2', 'Текст поста в формате Markdown...', 1);
----
--insert into tags(tag, post_id) values ('tag_1', 1);
--insert into tags(tag, post_id) values ('tag_2', 1);
--
--insert into tags(tag, post_id) values ('tag_2', );
--
--insert into comments(text, post_id) values ('Комментарий к посту 1', 1);
--insert into comments(text, post_id) values ('Комментарий к посту 2', 1);