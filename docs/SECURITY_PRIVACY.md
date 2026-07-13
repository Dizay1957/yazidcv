# Curriva security, privacy, and compliance baseline

Curriva is an assistive recruitment system. Evidence scores support human review and must never be the sole basis for hiring or rejection.

## Default controls

- Organization-scoped data access and administrator-only user/team provisioning
- Roles: administrator, recruiter, and hiring manager
- BCrypt password hashing (cost 12); 12–128 characters with uppercase, lowercase, number, and symbol
- Eight-hour signed access tokens, exact production CORS origin, restrictive response headers, and no browser exposure of provider credentials
- Private CV storage, file-type/size validation, safe filenames, SHA-256 duplicate detection, and immutable upload records
- Human review for uncertain extraction and mandatory reasons for rejected applications
- Audit schema for accountable changes; UTC timestamps and optimistic locking

## Required production operations

- Set unique `BOOTSTRAP_ADMIN_PASSWORD`, database credentials, and a random JWT secret through a secret manager. Remove or rotate the bootstrap credential after first access.
- Terminate TLS, restrict actuator and OpenAPI access, scan uploads for malware, encrypt database/storage backups, and test restoration.
- Define and document a retention period before launch. The database records retention date, consent timestamp, and legal basis; schedule deletion/anonymization only after legal review.
- Provide candidate privacy notice, access/correction/export/deletion request process, processor agreements, breach response, and authorized support contacts.
- Keep hosting and subprocessors consistent with applicable Moroccan Law 09-08 and, where applicable, GDPR. Obtain qualified legal review; this document is an engineering baseline, not legal advice.
- Do not store special-category data unless necessary and legally authorized. Limit exports by role and monitor them.

Publisher: Yasmid Studio, Marrakech, Morocco — contact@yasmid.com

