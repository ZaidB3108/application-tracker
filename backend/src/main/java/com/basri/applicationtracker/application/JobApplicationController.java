package com.basri.applicationtracker.application;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.basri.applicationtracker.activity.StatusHistory;
import com.basri.applicationtracker.activity.StatusHistoryRepository;
import com.basri.applicationtracker.activity.Interview;
import com.basri.applicationtracker.activity.InterviewRepository;
import com.basri.applicationtracker.activity.InterviewRequest;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class JobApplicationController {
    private final JobApplicationRepository repository;
    private final StatusHistoryRepository historyRepository;
    private final InterviewRepository interviewRepository;

    public JobApplicationController(JobApplicationRepository repository, StatusHistoryRepository historyRepository, InterviewRepository interviewRepository) {
        this.repository = repository; this.historyRepository = historyRepository; this.interviewRepository = interviewRepository;
    }

    @GetMapping
    public List<JobApplication> all(@RequestParam(required = false) Long userId) {
        return userId == null ? repository.findAllByOrderByAppliedDateDesc() : repository.findAllByUserIdOrderByAppliedDateDesc(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplication create(@Valid @RequestBody ApplicationRequest request) {
        JobApplication saved = repository.save(new JobApplication(request));
        historyRepository.save(new StatusHistory(saved.getId(), saved.getStatus()));
        return saved;
    }

    @PutMapping("/{id}")
    public JobApplication update(@PathVariable Long id, @Valid @RequestBody ApplicationRequest request) {
        JobApplication application = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Application not found: " + id));
        application.update(request);
        return repository.save(application);
    }

    @PatchMapping("/{id}/status")
    public JobApplication updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        JobApplication application = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Application not found: " + id));
        application.updateStatus(request.status());
        JobApplication saved = repository.save(application);
        historyRepository.save(new StatusHistory(saved.getId(), saved.getStatus()));
        return saved;
    }
    @PatchMapping("/{id}/notes")
    public JobApplication updateNotes(@PathVariable Long id, @RequestBody NotesUpdateRequest request) {
        JobApplication application = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Application not found: " + id));
        application.updateNotes(request.notes());
        return repository.save(application);
    }

    @GetMapping("/{id}/history") public List<StatusHistory> history(@PathVariable Long id) { return historyRepository.findAllByApplicationIdOrderByChangedAtDesc(id); }
    @GetMapping("/{id}/interviews") public List<Interview> interviews(@PathVariable Long id) { return interviewRepository.findAllByApplicationIdOrderByInterviewDateAsc(id); }
    @PostMapping("/{id}/interviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Interview scheduleInterview(@PathVariable Long id, @Valid @RequestBody InterviewRequest request) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Application not found: " + id);
        return interviewRepository.save(new Interview(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repository.deleteById(id); }
}
