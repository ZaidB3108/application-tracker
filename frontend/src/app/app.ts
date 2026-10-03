import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { forkJoin, switchMap } from 'rxjs';

type ApplicationStatus = 'SUBMITTED' | 'UNDER_REVIEW' | 'ONLINE_ASSESSMENT' | 'INTERVIEW' | 'OFFER' | 'REJECTED';

interface JobApplication {
  id: number;
  company: string;
  role: string;
  location: string;
  appliedDate: string;
  status: ApplicationStatus;
  logo: string;
}

interface ApiApplication { id: number; userId: number; companyId: number; roleTitle: string; workMode: string; appliedDate: string; status: ApplicationStatus; }
interface ApiCompany { id: number; name: string; location: string; }
interface StatusEvent { id: number; status: ApplicationStatus; changedAt: string; }
interface InterviewEvent { id: number; interviewType: string; interviewDate: string; interviewerName: string; result: string; notes: string; }

@Component({
  imports: [CommonModule, FormsModule],
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App {
  private readonly http = inject(HttpClient);
  protected readonly showProfile = signal(!localStorage.getItem('applyflow-profile-id'));
  protected profile = { fullName: '', email: '', password: '' };
  protected profileError = '';
  protected formError = '';
  protected readonly activityApplication = signal<JobApplication | null>(null);
  protected readonly statusEvents = signal<StatusEvent[]>([]);
  protected readonly interviewEvents = signal<InterviewEvent[]>([]);
  protected interviewDraft = { interviewType: 'Technical interview', interviewDate: '', interviewerName: '', notes: '' };
  protected readonly search = signal('');
  protected readonly showForm = signal(false);
  protected readonly applications = signal<JobApplication[]>([]);
  protected draft: Omit<JobApplication, 'id' | 'logo'> = this.emptyDraft();
  protected readonly visibleApplications = computed(() => {
    const query = this.search().trim().toLowerCase();
    return this.applications().filter((a) => !query || `${a.company} ${a.role}`.toLowerCase().includes(query));
  });
  protected readonly total = computed(() => this.applications().length);
  protected readonly interviews = computed(() => this.applications().filter((a) => a.status === 'INTERVIEW').length);
  protected readonly offers = computed(() => this.applications().filter((a) => a.status === 'OFFER').length);
  protected readonly responseRate = computed(() => {
    const responses = this.applications().filter((a) => !['SUBMITTED', 'UNDER_REVIEW'].includes(a.status)).length;
    return this.total() ? Math.round((responses / this.total()) * 100) : 0;
  });

  protected applicationsFor(status: ApplicationStatus) { return this.visibleApplications().filter((a) => a.status === status); }
  protected statusLabel(status: ApplicationStatus) { return status.replaceAll('_', ' ').replace(/\b\w/g, (letter) => letter.toUpperCase()); }
  protected addApplication() {
    if (!this.draft.company.trim() || !this.draft.role.trim()) return;
    const userId = Number(localStorage.getItem('applyflow-profile-id'));
    if (!userId) { this.formError = 'Create your profile before adding an application.'; return; }
    this.formError = '';
    const application = { ...this.draft, appliedDate: this.draft.appliedDate || new Date().toISOString().slice(0, 10) };
    this.http.post<{ id: number; name: string }>('http://localhost:8081/api/companies', {
      name: application.company, location: application.location,
    }).pipe(switchMap((company) => this.http.post<{ id: number }>('http://localhost:8081/api/applications', {
      userId, companyId: company.id, roleTitle: application.role, jobType: 'Full-time', workMode: application.location || 'Not specified',
      appliedDate: application.appliedDate, status: application.status, source: 'Applyflow', notes: '',
    }))).subscribe({
      next: (saved) => {
        this.applications.update((current) => [{ ...application, id: saved.id, logo: application.company.charAt(0).toUpperCase() }, ...current]);
        this.draft = this.emptyDraft(); this.showForm.set(false);
      },
      error: () => this.formError = 'Could not save the application. Check that the backend is running.',
    });
  }
  protected deleteApplication(id: number) {
    this.http.delete(`http://localhost:8081/api/applications/${id}`).subscribe({
      next: () => this.applications.update((current) => current.filter((a) => a.id !== id)),
      error: () => this.formError = 'Could not delete the application.',
    });
  }
  protected updateStatus(application: JobApplication, status: ApplicationStatus) {
    if (status === application.status) return;
    this.http.patch(`http://localhost:8081/api/applications/${application.id}/status`, { status }).subscribe({
      next: () => this.applications.update((current) => current.map((item) => item.id === application.id ? { ...item, status } : item)),
      error: () => this.formError = 'Could not update the application status.',
    });
  }
  protected openActivity(application: JobApplication) {
    this.activityApplication.set(application);
    forkJoin({
      history: this.http.get<StatusEvent[]>(`http://localhost:8081/api/applications/${application.id}/history`),
      interviews: this.http.get<InterviewEvent[]>(`http://localhost:8081/api/applications/${application.id}/interviews`),
    }).subscribe({ next: ({ history, interviews }) => { this.statusEvents.set(history); this.interviewEvents.set(interviews); } });
  }
  protected scheduleInterview() {
    const application = this.activityApplication();
    if (!application || !this.interviewDraft.interviewDate) return;
    this.http.post<InterviewEvent>(`http://localhost:8081/api/applications/${application.id}/interviews`, {
      ...this.interviewDraft, result: 'SCHEDULED',
    }).subscribe({
      next: (interview) => { this.interviewEvents.update((current) => [...current, interview]); this.interviewDraft = { interviewType: 'Technical interview', interviewDate: '', interviewerName: '', notes: '' }; },
      error: () => this.formError = 'Could not schedule the interview.',
    });
  }
  protected createProfile() {
    this.profileError = '';
    this.http.post<{ id: number }>('http://localhost:8081/api/profiles', this.profile).subscribe({
      next: (user) => { localStorage.setItem('applyflow-profile-id', String(user.id)); this.showProfile.set(false); this.loadApplications(); },
      error: () => this.profileError = 'We could not save your profile. Make sure the backend is running and use a new email address.',
    });
  }
  ngOnInit() { if (!this.showProfile()) this.loadApplications(); }
  private loadApplications() {
    const userId = Number(localStorage.getItem('applyflow-profile-id'));
    if (!userId) return;
    forkJoin({
      applications: this.http.get<ApiApplication[]>(`http://localhost:8081/api/applications?userId=${userId}`),
      companies: this.http.get<ApiCompany[]>('http://localhost:8081/api/companies'),
    }).subscribe({
      next: ({ applications, companies }) => {
        const companiesById = new Map(companies.map((company) => [company.id, company]));
        this.applications.set(applications.map((application) => {
          const company = companiesById.get(application.companyId);
          const name = company?.name ?? 'Unknown company';
          return { id: application.id, company: name, role: application.roleTitle, location: company?.location || application.workMode || 'Not specified', appliedDate: application.appliedDate, status: application.status, logo: name.charAt(0).toUpperCase() };
        }));
      },
      error: () => this.formError = 'Could not load saved applications. Check that the backend is running.',
    });
  }
  private emptyDraft(): Omit<JobApplication, 'id' | 'logo'> { return { company: '', role: '', location: '', appliedDate: '', status: 'SUBMITTED' }; }
}
