package com.basri.applicationtracker.application;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findAllByOrderByAppliedDateDesc();
    List<JobApplication> findAllByUserIdOrderByAppliedDateDesc(Long userId);
}
