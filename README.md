# Curriva

**Turn CVs into clear, reviewable candidate insight.**

Curriva is a multilingual, API-backed recruitment workspace published by Yasmid Studio in Marrakech, Morocco. It is built with Java 21, Spring Boot 3.5, React 19, PostgreSQL, Flyway, JWT security, and Docker.

## What works

- Database-backed administrator-managed users, companies, teams, BCrypt passwords, signed JWT login, and protected role-aware API routes
- Organization-scoped candidate, job, review, upload, and dashboard queries
- Candidate creation, validation, full-text search, editable profile details, archive/permanent deletion, and CSV export
- Job creation, persisted recruitment roles, detail pages, and job-by-job explainable candidate matching
- Job-specific candidate applications with an enforced Applied → Screening → Shortlisted → Interview/Assessment → Offer → Hired workflow
- Human review claiming and approval
- Multi-file PDF, DOC, and DOCX upload with size/type validation
- Apache Tika 3.3.1 plus Tesseract OCR text extraction with automatic candidate creation or email-based candidate reuse
- Extracted email, phone, candidate name, likely title, summary, and controlled-vocabulary skills
- Honest `OCR_REQUIRED` status for image-only CVs when Tesseract is unavailable
- Safe filenames, private storage, SHA-256 checksums, duplicate-binary rejection, and authenticated inline/download CV access from candidate profiles
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

On the configured Windows workstation, the checked OCR-aware shortcut is:

```powershell
.\scripts\start-backend.ps1
```

Terminal 2:

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

The local backend runs on `http://localhost:18765` because port 8080 is commonly occupied by other developer services. Vite proxies frontend `/api` requests to port 18765, avoiding cross-origin browser failures. Docker continues to expose the backend on port 8080.

Local bootstrap credentials (override them with environment variables before any shared deployment):

```text
admin@curriva.local
CurrivaAdmin!2026
```

The development database and uploaded files are stored under `data/` when launched from the repository root and are ignored by Git.

## Repository structure

```text
yazidcv/
├── backend/                 Spring Boot API and CV processing
├── frontend/                React, TypeScript and Vite application
│   └── vercel.json          Vercel SPA routing and browser security headers
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
- `BOOTSTRAP_ADMIN_EMAIL` and a unique `BOOTSTRAP_ADMIN_PASSWORD`; rotate the bootstrap password after first access
- `SEED_DEMO=false` in production
- `LINKEDIN_ACCESS_TOKEN` and `LINKEDIN_ORGANIZATION_URN` only after LinkedIn approves the organization as a Talent Solutions partner
- `INDEED_CLIENT_ID` and `INDEED_CLIENT_SECRET` only after Indeed approves the ATS integration

Terminate TLS at a trusted reverse proxy. Rotate secrets through a secret manager, restrict actuator networking, back up PostgreSQL and the upload volume, and configure malware scanning before Internet exposure.

## Deploying the frontend on Vercel

Vercel can host the React frontend, but **deploying this repository only to Vercel is not a complete Curriva deployment**. The Java API, PostgreSQL database, Tesseract OCR process, and private persistent CV storage must run on a container/VM platform with a durable volume (for example, Render, Railway, Fly.io, a cloud container service, or a VPS).

Create the Vercel project with these settings:

| Setting | Value |
| --- | --- |
| Root Directory | `frontend` |
| Framework Preset | Vite |
| Build Command | `npm run build` |
| Output Directory | `dist` |
| Environment variable | `VITE_API_URL=https://api.your-domain.com/api/v1` |

`frontend/vercel.json` supplies the SPA fallback required for direct visits to client-side routes and adds baseline browser security headers. `VITE_API_URL` is public browser configuration, so it must never contain a password, token, or other secret.

On the backend host, set all variables listed under **Production configuration**, plus:

```text
CORS_ORIGIN=https://your-project.vercel.app
UPLOAD_DIRECTORY=/path/to/a/private-durable-volume
```

Use the final custom frontend domain for `CORS_ORIGIN` when one is configured. The database and upload directory must survive restarts and deployments. Run the backend container from `backend/Dockerfile`; it already includes the English, French, and Arabic Tesseract language data. Before exposing the frontend, verify `https://api.your-domain.com/actuator/health`, sign in through the deployed UI, upload and open a CV, create a job, move an application through the workflow, and test English, French, and Arabic/RTL views.

See [Windows OCR setup](docs/OCR_SETUP_WINDOWS.md), [recruitment workflow](docs/RECRUITMENT_WORKFLOW.md), and [security/privacy baseline](docs/SECURITY_PRIVACY.md).

## Honest current boundary

The implemented product slice is operational for the screens exposed in the frontend. New installations do not seed fake candidates, jobs, reviews, charts, or failure counts; dashboard values are calculated from persisted records. Existing development databases retain previous records intentionally.

LinkedIn Apply Connect and Indeed Job Sync cannot be activated through code alone: both require partner approval, legal agreements, OAuth credentials, and provider certification. The Settings screen reports actual server configuration readiness and links to official onboarding. It never represents an unapproved provider as connected.

MinIO, RabbitMQ retry queues, OpenSearch, semantic embeddings, automated retention execution, and AI-assisted extraction remain separate deployment phases and are not falsely represented as complete. Hiring decisions remain human-controlled.

Apache Tika extraction is implemented for text-based PDF, DOC, and DOCX CVs. Image-only/scanned documents require Tesseract with English, French, and Arabic language packs available on the backend host. Without that external native dependency the document remains in `OCR_REQUIRED`; Curriva does not fabricate extracted content.
