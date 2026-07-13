create table candidate_applications (
 id uuid primary key, organization_id uuid not null references organizations(id), candidate_id uuid not null references candidates(id),
 job_id uuid not null references jobs(id), stage varchar(30) not null, owner_id uuid references app_users(id),
 rejection_reason varchar(500), notes varchar(2000), version bigint not null default 0,
 created_at timestamp with time zone not null, updated_at timestamp with time zone not null,
 unique(candidate_id, job_id)
);
create index idx_application_org_stage on candidate_applications(organization_id,stage);
create index idx_application_job on candidate_applications(job_id);

