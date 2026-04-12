package com.wu.repo;

import com.wu.model.Difficulty;
import com.wu.model.MasteryLevel;
import com.wu.model.StudyRecord;
import com.wu.model.StudyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学习记录数据访问层
 */
@Repository
public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long>, JpaSpecificationExecutor<StudyRecord> {

    /**
     * 根据用户ID查询所有学习记录
     */
    List<StudyRecord> findByUserUserIdOrderByStudyDateDesc(Long userId);

    /**
     * 根据ID和用户ID查询学习记录
     */
    Optional<StudyRecord> findByStudyIdAndUserUserId(Long studyId, Long userId);

    /**
     * 根据用户ID和学习类型查询
     */
    List<StudyRecord> findByUserUserIdAndStudyType(Long userId, StudyType studyType);

    /**
     * 根据用户ID和难度查询
     */
    List<StudyRecord> findByUserUserIdAndDifficulty(Long userId, Difficulty difficulty);

    /**
     * 根据用户ID和掌握程度查询
     */
    List<StudyRecord> findByUserUserIdAndMasteryLevel(Long userId, MasteryLevel masteryLevel);
}
