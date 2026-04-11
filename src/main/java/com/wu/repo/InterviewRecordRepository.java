package com.wu.repo;

import com.wu.model.InterviewRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface InterviewRecordRepository extends JpaRepository<InterviewRecord, Long>, JpaSpecificationExecutor<InterviewRecord> {
    List<InterviewRecord> findByUserUserId(Long userId);
}
