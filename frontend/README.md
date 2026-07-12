# YazidCV frontend

Recruiter-focused React interface for CV intelligence, candidate search, human review, and job matching.

## Run locally

```bash
npm install
npm run dev
```

Open the local URL printed by Vite. Use `npm run build` for a production build.

Vite proxies `/api` to the development backend at `http://localhost:18765`, avoiding browser CORS issues. Override the full endpoint with `VITE_API_URL` when required.

The current screens use representative local data and are ready to be connected to the `/api/v1` backend through a centralized API client in the integration phase.
