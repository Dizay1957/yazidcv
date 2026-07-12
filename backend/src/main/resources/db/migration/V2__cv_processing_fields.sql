alter table cv_documents add column extracted_text text;
alter table cv_documents add column extraction_method varchar(40);
alter table cv_documents add column processing_error varchar(1000);
alter table cv_documents add column completed_at timestamp with time zone;
create index idx_cv_org_status on cv_documents(organization_id, status);
