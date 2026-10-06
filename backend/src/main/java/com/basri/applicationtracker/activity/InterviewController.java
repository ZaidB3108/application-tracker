package com.basri.applicationtracker.activity;

import com.basri.applicationtracker.application.JobApplicationRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "http://localhost:4200")
public class InterviewController {
    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository applicationRepository;

    public InterviewController(InterviewRepository interviewRepository, JobApplicationRepository applicationRepository) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping
    public List<Interview> upcoming(@RequestParam Long userId) {
        List<Long> applicationIds = applicationRepository.findAllByUserIdOrderByAppliedDateDesc(userId)
            .stream().map(application -> application.getId()).toList();
        return applicationIds.isEmpty() ? List.of() : interviewRepository.findAllByApplicationIdInOrderByInterviewDateAsc(applicationIds);
    }
}
