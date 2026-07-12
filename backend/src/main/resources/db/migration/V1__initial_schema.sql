create table organizations (
  id uuid primary key, name varchar(150) not null, slug varchar(100) not null unique, active boolean not null default true,
  created_at timestamp with time zone not null, updated_at timestamp with time zone not null
);
create table candidates (
  id uuid primary key, organization_id uuid not null references organizations(id), first_name varchar(100) not null, last_name varchar(100) not null,
  email varchar(255), phone varchar(50), current_title varchar(180), current_company varchar(180), city varchar(100), country varchar(100),
  years_experience integer not null default 0, status varchar(40) not null, source varchar(40) not null, professional_summary varchar(4000),
  skills varchar(2000), match_score integer not null default 0, active boolean not null default true, version bigint not null default 0,
  created_at timestamp with time zone not null, updated_at timestamp with time zone not null
);
create index idx_candidate_org on candidates(organization_id); create index idx_candidate_name on candidates(last_name, first_name);
create table jobs (
  id uuid primary key, organization_id uuid not null references organizations(id), title varchar(180) not null, department varchar(120), location varchar(180),
  workplace_type varchar(30), description varchar(6000), required_skills varchar(2000), status varchar(30) not null, candidate_count integer not null default 0,
  closing_date date, version bigint not null default 0, created_at timestamp with time zone not null, updated_at timestamp with time zone not null
);
create index idx_job_org on jobs(organization_id);
create table review_tasks (
  id uuid primary key, organization_id uuid not null references organizations(id), candidate_id uuid not null references candidates(id), reason varchar(500) not null,
  confidence integer not null, priority varchar(20) not null, status varchar(30) not null, reviewer_email varchar(255), notes varchar(2000),
  created_at timestamp with time zone not null, updated_at timestamp with time zone not null
);
create index idx_review_org_status on review_tasks(organization_id,status);
create table cv_documents (
  id uuid primary key, organization_id uuid not null references organizations(id), candidate_id uuid references candidates(id), original_filename varchar(255) not null,
  storage_key varchar(500) not null unique, content_type varchar(120) not null, file_size bigint not null, checksum varchar(64) not null,
  status varchar(40) not null, uploaded_by varchar(255) not null, created_at timestamp with time zone not null, updated_at timestamp with time zone not null,
  unique(organization_id, checksum)
);
create table audit_events (
  id uuid primary key, organization_id uuid not null references organizations(id), actor varchar(255) not null, action varchar(80) not null,
  entity_type varchar(80) not null, entity_id varchar(80), summary varchar(500) not null, correlation_id varchar(100), created_at timestamp with time zone not null
);
