package com.basri.applicationtracker.activity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "status_history")
public class StatusHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "application_id", nullable = false) private Long applicationId;
    @Column(nullable = false) private String status;
    @Column(name = "changed_at", nullable = false) private LocalDateTime changedAt;
    protected StatusHistory() { }
    public StatusHistory(Long applicationId, String status) { this.applicationId = applicationId; this.status = status; this.changedAt = LocalDateTime.now(); }
    public Long getId() { return id; } public Long getApplicationId() { return applicationId; }
    public String getStatus() { return status; } public LocalDateTime getChangedAt() { return changedAt; }
}
