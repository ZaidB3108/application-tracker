package com.basri.applicationtracker.activity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> { List<StatusHistory> findAllByApplicationIdOrderByChangedAtDesc(Long applicationId); }
