create table teams (
  id uuid primary key, organization_id uuid not null references organizations(id), name varchar(120) not null,
  description varchar(500), active boolean not null default true, created_at timestamp with time zone not null,
  updated_at timestamp with time zone not null, unique(organization_id, name)
);
create index idx_team_org on teams(organization_id);

create table app_users (
  id uuid primary key, organization_id uuid not null references organizations(id), team_id uuid references teams(id),
  first_name varchar(100) not null, last_name varchar(100) not null, email varchar(255) not null unique,
  password_hash varchar(100) not null, role varchar(30) not null, locale varchar(5) not null default 'en',
  active boolean not null default true, password_changed_at timestamp with time zone not null,
  failed_login_attempts integer not null default 0, locked_until timestamp with time zone,
  created_at timestamp with time zone not null, updated_at timestamp with time zone not null
);
create index idx_user_org on app_users(organization_id);
create index idx_user_team on app_users(team_id);

alter table candidates add column retention_until date;
alter table candidates add column consent_recorded_at timestamp with time zone;
alter table candidates add column legal_basis varchar(40) not null default 'LEGITIMATE_INTEREST';

