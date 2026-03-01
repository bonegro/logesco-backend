package com.schools.logesco.log.repository;

import com.schools.logesco.log.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
}

