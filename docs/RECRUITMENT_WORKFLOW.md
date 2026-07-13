# Recruitment workflow

Every application links one candidate to one job. Candidate profiles and applications are intentionally separate: one person may apply to multiple roles and progress differently in each.

1. **Applied** — received manually, from a CV upload, or a future approved integration.
2. **Screening** — recruiter verifies identity, consent/legal basis, minimum criteria, and extracted evidence.
3. **Shortlisted** — recruiter confirms documented criteria; a hiring manager may review.
4. **Interview** — structured interview using the job scorecard.
5. **Assessment** — optional work sample or technical assessment. It can return to Interview or proceed to Offer.
6. **Offer** — approved offer issued and tracked outside automatic scoring.
7. **Hired** — accepted offer; terminal state.

At any non-terminal stage the candidate can be **Rejected** (a reason is mandatory) or **Withdrawn**. Hired, Rejected, and Withdrawn are terminal to preserve history. Reopening requires an explicit new application or a future audited administrator action.

Curriva prevents skipped stages and invalid transitions in the API. Matching and extraction scores remain explainable review aids; people make every employment decision.

