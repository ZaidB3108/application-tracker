package com.basri.applicationtracker.application;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Maps the existing applyflow_db.public.applications table. */
@Entity
@Table(name = "applications")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "company_id", nullable = false)
    private Long companyId;
    @Column(name = "role_title", nullable = false)
    private String roleTitle;
    @Column(name = "job_type")
    private String jobType;
    @Column(name = "work_mode")
    private String workMode;
    @Column(name = "salary_min")
    private Integer salaryMin;
    @Column(name = "salary_max")
    private Integer salaryMax;
    @Column(name = "job_posting_url")
    private String jobPostingUrl;
    private String source;
    @Column(name = "applied_date")
    private LocalDate appliedDate;
    @Column(nullable = false)
    private String status;
    @Column(columnDefinition = "text")
    private String notes;
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    protected JobApplication() { }

    public JobApplication(ApplicationRequest request) { update(request); }

    public void update(ApplicationRequest request) {
        userId = request.userId(); companyId = request.companyId(); roleTitle = request.roleTitle();
        jobType = request.jobType(); workMode = request.workMode(); salaryMin = request.salaryMin();
        salaryMax = request.salaryMax(); jobPostingUrl = request.jobPostingUrl(); source = request.source();
        appliedDate = request.appliedDate(); status = request.status(); notes = request.notes();
    }
    public void updateStatus(String newStatus) { status = newStatus; }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getCompanyId() { return companyId; }
    public String getRoleTitle() { return roleTitle; }
    public String getJobType() { return jobType; }
    public String getWorkMode() { return workMode; }
    public Integer getSalaryMin() { return salaryMin; }
    public Integer getSalaryMax() { return salaryMax; }
    public String getJobPostingUrl() { return jobPostingUrl; }
    public String getSource() { return source; }
    public LocalDate getAppliedDate() { return appliedDate; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
