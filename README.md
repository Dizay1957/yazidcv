# YazidCV

An API-backed CV intelligence and recruitment workspace built with Java 21, Spring Boot 3.5, React 19, PostgreSQL, Flyway, JWT security, and Docker.

## What works

- Signed JWT login and protected API routes
- Organization-scoped candidate, job, review, upload, and dashboard queries
- Candidate creation, validation, search, profile details, and CSV export
- Job creation and persisted recruitment roles
- Human review claiming and approval
- Multi-file PDF, DOC, and DOCX upload with size/type validation
- Apache Tika 3.3.1 text extraction with automatic candidate creation or email-based candidate reuse
- Extracted email, phone, candidate name, likely title, summary, and controlled-vocabulary skills
- Honest `OCR_REQUIRED` status for image-only CVs when Tesseract is unavailable
- Safe filenames, private storage, SHA-256 checksums, and duplicate-binary rejection
- Flyway migrations, optimistic locking, consistent API errors, OpenAPI, health probes, and Prometheus metrics
- Responsive React workspace with loading, error, empty, session-expiry, and reduced-motion states
- Persistent English, French, and Arabic UI selection with full document direction switching and RTL-safe responsive layouts
- Server-reported LinkedIn Apply Connect and Indeed Job Sync onboarding readiness without exposing integration credentials
- PostgreSQL production profile and an immediately runnable H2 development profile

## Local development

Requirements: Java 21, Maven 3.9+, and Node 22+.

Terminal 1 (run from the repository root, not `frontend`):

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
mvn -pl backend spring-boot:run
```

Terminal 2:

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

The local backend runs on `http://localhost:18765` because port 8080 is commonly occupied by other developer services. Vite proxies frontend `/api` requests to port 18765, avoiding cross-origin browser failures. Docker continues to expose the backend on port 8080.

Demo credentials:

```text
admin@yazidcv.dev
YazidCV2026!
```

The development database and uploaded files are stored under `data/` when launched from the repository root and are ignored by Git.

## Repository structure

```text
yazidcv/
├── backend/                 Spring Boot API and CV processing
├── frontend/                React, TypeScript and Vite application
├── docker-compose.yml       PostgreSQL production-style local stack
├── .env.example             Safe configuration template
└── pom.xml                  Maven reactor
```

## Docker with PostgreSQL

```powershell
Copy-Item .env.example .env
# Replace DATABASE_PASSWORD and JWT_SECRET in .env
docker compose up --build
```

- Frontend: `http://localhost:3000`
- Backend: `http://localhost:8080`
- OpenAPI: `http://localhost:8080/swagger-ui.html`
- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/prometheus`

## Verification

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
mvn test
cd frontend
npm ci
npm run typecheck
npm run build
```

The backend integration suite verifies unauthorized access, login, JWT use, tenant-scoped candidates, live dashboard aggregation, CV text extraction, and automatic candidate creation.

## Security reporting

Do not open public issues containing CV data, credentials, signed URLs, or security-sensitive details. Report sensitive findings privately to the repository owner through GitHub.

## Contributing

Create a focused branch, include tests for behavioral changes, and run the verification commands above before opening a pull request. Never commit `.env`, local databases, CV files, generated build output, or provider credentials.

## Production configuration

Set at minimum:

- `SPRING_PROFILES_ACTIVE=prod`
- `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`
- `JWT_SECRET` with at least 32 random bytes
- `CORS_ORIGIN` to the exact frontend origin
- `UPLOAD_DIRECTORY` to a durable private volume
- `SEED_DEMO=false` after provisioning real identity and organization records
- `LINKEDIN_ACCESS_TOKEN` and `LINKEDIN_ORGANIZATION_URN` only after LinkedIn approves the organization as a Talent Solutions partner
- `INDEED_CLIENT_ID` and `INDEED_CLIENT_SECRET` only after Indeed approves the ATS integration

Terminate TLS at a trusted reverse proxy. Rotate secrets through a secret manager, restrict actuator networking, back up PostgreSQL and the upload volume, and replace the demo in-memory account with the organization identity provider before Internet exposure.

## Honest current boundary

The implemented product slice is operational for the screens exposed in the frontend. New installations do not seed fake candidates, jobs, reviews, charts, or failure counts; dashboard values are calculated from persisted records. Existing development databases retain previous records intentionally.

LinkedIn Apply Connect and Indeed Job Sync cannot be activated through code alone: both require partner approval, legal agreements, OAuth credentials, and provider certification. The Settings screen reports actual server configuration readiness and links to official onboarding. It never represents an unapproved provider as connected.

Native OCR execution, MinIO, RabbitMQ retry queues, OpenSearch, semantic embeddings, automated retention, and AI-assisted extraction from the original master brief remain separate backend phases and are not falsely represented as complete. Hiring decisions remain human-controlled.

Apache Tika extraction is now implemented for text-based PDF, DOC, and DOCX CVs. Image-only/scanned documents require Tesseract with English, French, and Arabic language packs available on the backend host. Without that external native dependency the document remains in `OCR_REQUIRED`; YazidCV does not fabricate extracted content.
