
create database searchengineapp;
show databases;
use searchengineapp;

create table pages(

pageTitle varchar(200),
pageLink text,
pageText mediumText);
select * from pages;
 select pageTitle, pageLink,(length(lower(pageText))-length(replace(pageText, 'java','')) ) as countoccurence from pages order by countoccurence desc limit 30;

create table history(
keyword text,
link text
);
select * from history
