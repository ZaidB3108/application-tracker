package com.basri.applicationtracker.activity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name = "interviews")
public class Interview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "application_id", nullable = false) private Long applicationId;
    @Column(name = "interview_type", nullable = false) private String interviewType;
    @Column(name = "interview_date", nullable = false) private LocalDateTime interviewDate;
    @Column(name = "interviewer_name") private String interviewerName;
    private String result;
    @Column(columnDefinition = "text") private String notes;
    @Column(name = "created_at", insertable = false, updatable = false) private LocalDateTime createdAt;
    protected Interview() { }
    public Interview(Long applicationId, InterviewRequest request) {
        this.applicationId = applicationId; this.interviewType = request.interviewType(); this.interviewDate = request.interviewDate();
        this.interviewerName = request.interviewerName(); this.result = request.result(); this.notes = request.notes();
    }
    public Long getId() { return id; } public Long getApplicationId() { return applicationId; }
    public String getInterviewType() { return interviewType; } public LocalDateTime getInterviewDate() { return interviewDate; }
    public String getInterviewerName() { return interviewerName; } public String getResult() { return result; } public String getNotes() { return notes; }
}
