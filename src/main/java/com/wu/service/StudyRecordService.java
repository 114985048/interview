package com.wu.service;

import com.wu.model.*;
import com.wu.repo.StudyRecordRepository;
import com.wu.repository.UserRepository;
import com.wu.web.dto.StudyRecordForm;
import com.wu.web.dto.StudySearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 学习记录服务层
 */
@Service
public class StudyRecordService {

    @Autowired
    private StudyRecordRepository studyRecordRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据用户ID查询所有学习记录
     */
    public List<StudyRecord> findByUserId(Long userId) {
        return studyRecordRepository.findByUserUserIdOrderByStudyDateDesc(userId);
    }

    /**
     * 根据ID查询学习记录（带权限检查）
     */
    public StudyRecord findById(Long studyId, User user) {
        StudyRecord record = studyRecordRepository.findById(studyId)
                .orElse(null);
        if (record == null) {
            return null;
        }
        // 检查权限：管理员可以查看所有记录，普通用户只能查看自己的记录
        if (!user.isAdmin() && !record.getUser().getUserId().equals(user.getUserId())) {
            return null;
        }
        return record;
    }

    /**
     * 搜索学习记录
     */
    public List<StudyRecord> search(StudySearchForm searchForm, User user) {
        Specification<StudyRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 如果不是管理员，只返回用户自己的记录
            if (!user.isAdmin()) {
                predicates.add(cb.equal(root.get("user").get("userId"), user.getUserId()));
            }

            // 学习主题模糊查询
            if (searchForm.getSubject() != null && !searchForm.getSubject().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("subject")), "%" + searchForm.getSubject().trim().toLowerCase() + "%"));
            }

            // 学习类型
            if (searchForm.getStudyType() != null) {
                predicates.add(cb.equal(root.get("studyType"), searchForm.getStudyType()));
            }

            // 难度等级
            if (searchForm.getDifficulty() != null) {
                predicates.add(cb.equal(root.get("difficulty"), searchForm.getDifficulty()));
            }

            // 掌握程度
            if (searchForm.getMasteryLevel() != null) {
                predicates.add(cb.equal(root.get("masteryLevel"), searchForm.getMasteryLevel()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 排序
        String sortBy = searchForm.getSortBy();
        if (!"subject".equals(sortBy) && !"duration".equals(sortBy)) {
            sortBy = "studyDate";
        }
        Sort.Direction direction = "asc".equalsIgnoreCase(searchForm.getDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        return studyRecordRepository.findAll(spec, sort);
    }

    /**
     * 保存学习记录
     */
    @Transactional
    public StudyRecord save(StudyRecordForm form, User user) {
        StudyRecord record;
        if (form.getStudyId() != null) {
            // 编辑模式：先查询记录
            record = studyRecordRepository.findById(form.getStudyId())
                    .orElseThrow(() -> new RuntimeException("学习记录不存在"));
            // 检查权限：管理员可以修改所有记录，普通用户只能修改自己的记录
            if (!user.isAdmin() && !record.getUser().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("学习记录不存在");
            }
        } else {
            // 新增模式
            record = new StudyRecord();
            record.setUser(user);
        }

        record.setSubject(form.getSubject());
        record.setStudyType(form.getStudyType());
        record.setDifficulty(form.getDifficulty());
        record.setMasteryLevel(form.getMasteryLevel());
        record.setDuration(form.getDuration());
        record.setProgress(form.getProgress());
        record.setContent(form.getContent());
        record.setKeyPoints(form.getKeyPoints());
        record.setQuestions(form.getQuestions());
        record.setStudyDate(form.getStudyDate());

        return studyRecordRepository.save(record);
    }

    /**
     * 删除学习记录
     */
    @Transactional
    public void delete(Long studyId, User user) {
        StudyRecord record = studyRecordRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("学习记录不存在"));
        // 检查权限：管理员可以删除所有记录，普通用户只能删除自己的记录
        if (!user.isAdmin() && !record.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("学习记录不存在");
        }
        studyRecordRepository.delete(record);
    }

    /**
     * 统计学习时长
     */
    public int getTotalStudyTime(Long userId) {
        return findByUserId(userId).stream()
                .mapToInt(StudyRecord::getDuration)
                .sum();
    }

    /**
     * 统计学习记录数量
     */
    public long getStudyCount(Long userId) {
        return studyRecordRepository.count(
                (root, query, cb) -> cb.equal(root.get("user").get("userId"), userId)
        );
    }
}
