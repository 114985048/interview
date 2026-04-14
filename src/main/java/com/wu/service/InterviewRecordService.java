package com.wu.service;

import com.wu.model.InterviewRecord;
import com.wu.model.User;
import com.wu.repo.InterviewRecordRepository;
import com.wu.web.dto.InterviewRecordForm;
import com.wu.web.dto.InterviewSearchForm;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InterviewRecordService {

    private final InterviewRecordRepository interviewRecordRepository;

    public InterviewRecordService(InterviewRecordRepository interviewRecordRepository) {
        this.interviewRecordRepository = interviewRecordRepository;
    }

    @Transactional
    public InterviewRecord create(InterviewRecordForm form, User user) {
        InterviewRecord record = new InterviewRecord();
        record.setUser(user);
        applyForm(record, form);
        return interviewRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public List<InterviewRecord> search(InterviewSearchForm form, User user) {
        Specification<InterviewRecord> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 如果不是管理员，只返回用户自己的记录
            if (!user.isAdmin()) {
                predicates.add(cb.equal(root.get("user").get("userId"), user.getUserId()));
            }
            if (form.getCompanyName() != null && !form.getCompanyName().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("companyName")), "%" + form.getCompanyName().trim().toLowerCase() + "%"));
            }
            if (form.getCompanyType() != null) {
                predicates.add(cb.equal(root.get("companyType"), form.getCompanyType()));
            }
            if (form.getInterviewResult() != null) {
                predicates.add(cb.equal(root.get("interviewResult"), form.getInterviewResult()));
            }
            if (form.getPosition() != null && !form.getPosition().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("position")), "%" + form.getPosition().trim().toLowerCase() + "%"));
            }
            if (form.getSalaryRange() != null && !form.getSalaryRange().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("salaryRange")), "%" + form.getSalaryRange().trim().toLowerCase() + "%"));
            }
            if (form.getDifficulty() != null) {
                predicates.add(cb.equal(root.get("difficulty"), form.getDifficulty()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return interviewRecordRepository.findAll(specification, buildSort(form));
    }

    @Transactional(readOnly = true)
    public InterviewRecord findById(Long interviewId, User user) {
        InterviewRecord record = interviewRecordRepository.findById(interviewId)
                .orElseThrow(() -> new EntityNotFoundException("该记录不存在或已被删除"));
        // 检查权限：管理员可以查看所有记录，普通用户只能查看自己的记录
        if (!user.isAdmin() && !record.getUser().getUserId().equals(user.getUserId())) {
            throw new EntityNotFoundException("该记录不存在或已被删除");
        }
        return record;
    }

    @Transactional
    public InterviewRecord update(Long interviewId, InterviewRecordForm form, User user) {
        InterviewRecord record = interviewRecordRepository.findById(interviewId)
                .orElseThrow(() -> new EntityNotFoundException("该记录不存在或已被删除"));
        // 检查权限：管理员可以修改所有记录，普通用户只能修改自己的记录
        if (!user.isAdmin() && !record.getUser().getUserId().equals(user.getUserId())) {
            throw new EntityNotFoundException("该记录不存在或已被删除");
        }
        applyForm(record, form);
        return interviewRecordRepository.save(record);
    }

    @Transactional
    public void delete(Long interviewId, User user) {
        InterviewRecord record = interviewRecordRepository.findById(interviewId)
                .orElseThrow(() -> new EntityNotFoundException("该记录不存在或已被删除"));
        // 检查权限：管理员可以删除所有记录，普通用户只能删除自己的记录
        if (!user.isAdmin() && !record.getUser().getUserId().equals(user.getUserId())) {
            throw new EntityNotFoundException("该记录不存在或已被删除");
        }
        interviewRecordRepository.delete(record);
    }

    public InterviewRecordForm toForm(InterviewRecord record) {
        InterviewRecordForm form = new InterviewRecordForm();
        form.setInterviewId(record.getInterviewId());
        form.setCompanyName(record.getCompanyName());
        form.setCompanyType(record.getCompanyType());
        form.setPosition(record.getPosition());
        form.setSalaryRange(record.getSalaryRange());
        form.setCompanyAddress(record.getCompanyAddress());
        form.setInterviewDate(record.getInterviewDate());
        form.setInterviewResult(record.getInterviewResult());
        form.setInterviewRound(record.getInterviewRound());
        form.setDifficulty(record.getDifficulty());
        form.setInterviewContent(record.getInterviewContent());
        form.setSummary(record.getSummary());
        form.setQuestions(record.getQuestions());
        return form;
    }

    private void applyForm(InterviewRecord record, InterviewRecordForm form) {
        record.setCompanyName(form.getCompanyName().trim());
        record.setCompanyType(form.getCompanyType());
        record.setPosition(form.getPosition());
        record.setSalaryRange(form.getSalaryRange());
        record.setCompanyAddress(form.getCompanyAddress());
        record.setInterviewDate(form.getInterviewDate());
        record.setInterviewResult(form.getInterviewResult());
        record.setInterviewRound(form.getInterviewRound());
        record.setDifficulty(form.getDifficulty());
        record.setInterviewContent(form.getInterviewContent());
        record.setSummary(form.getSummary());
        record.setQuestions(form.getQuestions());
    }

    private Sort buildSort(InterviewSearchForm form) {
        String sortBy = form.getSortBy();
        if (!"companyName".equals(sortBy) && !"salaryRange".equals(sortBy)) {
            sortBy = "interviewDate";
        }
        Sort.Direction direction = "asc".equalsIgnoreCase(form.getDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, sortBy);
    }
}
