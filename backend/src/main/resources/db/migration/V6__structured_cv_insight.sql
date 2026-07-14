alter table candidates add column linkedin_url varchar(500);
alter table candidates add column github_url varchar(500);
alter table candidates add column spoken_languages varchar(1000);
alter table candidates add column education_summary varchar(4000);
alter table candidates add column experience_summary varchar(6000);
alter table candidates add column projects_summary varchar(6000);
alter table candidates add column extraction_confidence integer not null default 0;
alter table cv_documents add column parser_version varchar(30);
alter table cv_documents add column extraction_confidence integer not null default 0;

