const API_URL = import.meta.env.VITE_API_URL ?? '/api/v1'

export type User = { firstName: string; lastName: string; email: string; role: string; organization: string }
export type Candidate = { id: string; firstName: string; lastName: string; fullName: string; initials: string; email?: string; phone?: string; currentTitle: string; currentCompany?: string; city?: string; country?: string; yearsExperience: number; status: string; source: string; professionalSummary?: string; skills: string[]; matchScore: number; linkedinUrl?: string; githubUrl?: string; spokenLanguages: string[]; educationSummary?: string; experienceSummary?: string; projectsSummary?: string; extractionConfidence: number; createdAt: string; updatedAt: string }
export type Job = { id: string; title: string; department?: string; location?: string; workplaceType?: string; description?: string; requiredSkills: string[]; status: string; candidateCount: number; closingDate?: string; updatedAt: string }
export type Review = { id: string; candidateId: string; candidateName: string; initials: string; currentTitle: string; reason: string; confidence: number; priority: string; status: string; createdAt: string }
export type CvDocument = { id: string; candidateId?: string; originalFilename: string; contentType: string; fileSize: number; status: string; extractionMethod?: string; processingError?: string; uploadedAt: string; completedAt?: string }
export type Dashboard = { openJobs: number; activeCandidates: number; awaitingReview: number; failedProcessing: number; candidatesThisWeek: number; averageProcessingSeconds: number; activity: { day: string; applications: number }[] }
export type IntegrationStatus = { provider: 'LINKEDIN' | 'INDEED'; name: string; status: 'CONFIGURED' | 'ACTION_REQUIRED'; configured: boolean; requirement: string; onboardingUrl: string }
export type Team = { id: string; name: string; description?: string }
export type ManagedUser = { id: string; firstName: string; lastName: string; email: string; role: string; teamId?: string; locale: string; active: boolean }
export type Page<T> = { content: T[]; totalElements: number; totalPages: number; number: number }

const tokenKey = 'yazidcv_access_token'
export const session = {
  token: () => sessionStorage.getItem(tokenKey),
  user: (): User | null => { try { return JSON.parse(sessionStorage.getItem('yazidcv_user') ?? 'null') } catch { return null } },
  clear: () => { sessionStorage.removeItem(tokenKey); sessionStorage.removeItem('yazidcv_user') },
}

export class ApiError extends Error { constructor(public status: number, message: string) { super(message) } }

async function request<T>(path: string, init: RequestInit = {}): Promise<T> {
  const headers = new Headers(init.headers)
  const token = session.token()
  if (token) headers.set('Authorization', `Bearer ${token}`)
  if (init.body && !(init.body instanceof FormData)) headers.set('Content-Type', 'application/json')
  const response = await fetch(`${API_URL}${path}`, { ...init, headers })
  if (response.status === 401 && path !== '/auth/login') { session.clear(); window.dispatchEvent(new Event('yazidcv:unauthorized')) }
  if (!response.ok) { const body = await response.json().catch(() => null); throw new ApiError(response.status, body?.message ?? 'The request could not be completed') }
  if (response.status === 204) return undefined as T
  return response.json()
}

export const api = {
  login: async (email: string, password: string) => { const data = await request<{ accessToken: string; user: User }>('/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) }); sessionStorage.setItem(tokenKey, data.accessToken); sessionStorage.setItem('yazidcv_user', JSON.stringify(data.user)); return data },
  dashboard: () => request<Dashboard>('/dashboard'),
  candidates: (q = '') => request<Page<Candidate>>(`/candidates?size=100&q=${encodeURIComponent(q)}`),
  createCandidate: (data: object) => request<Candidate>('/candidates', { method: 'POST', body: JSON.stringify(data) }),
  updateCandidate: (id: string, data: object) => request<Candidate>(`/candidates/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  archiveCandidate: (id: string) => request<void>(`/candidates/${id}`, { method: 'DELETE' }),
  jobs: () => request<Page<Job>>('/jobs?size=100'),
  createJob: (data: object) => request<Job>('/jobs', { method: 'POST', body: JSON.stringify(data) }),
  updateJob: (id: string, data: object) => request<Job>(`/jobs/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  reviews: () => request<Review[]>('/reviews'),
  updateReview: (id: string, status: string, notes = '') => request<Review>(`/reviews/${id}`, { method: 'PATCH', body: JSON.stringify({ status, notes }) }),
  cvs: () => request<Page<CvDocument>>('/cvs?size=100'),
  integrations: () => request<IntegrationStatus[]>('/integrations'),
  teams: () => request<Team[]>('/admin/teams'),
  users: () => request<ManagedUser[]>('/admin/users'),
  createTeam: (data: object) => request<Team>('/admin/teams', { method: 'POST', body: JSON.stringify(data) }),
  createUser: (data: object) => request<ManagedUser>('/admin/users', { method: 'POST', body: JSON.stringify(data) }),
  deactivateUser: (id: string) => request<void>(`/admin/users/${id}`, { method: 'DELETE' }),
  upload: (files: File[]) => { const form = new FormData(); files.forEach(file => form.append('files', file)); return request<CvDocument[]>('/cvs', { method: 'POST', body: form }) },
}
