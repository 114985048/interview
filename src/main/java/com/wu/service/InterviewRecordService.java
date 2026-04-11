package com.wu.service;

import com.wu.model.InterviewQuestion;
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
        System.out.println("search方法 - 用户: " + user.getUsername() + ", ID: " + user.getUserId() + ", isAdmin: " + user.isAdmin());
        Specification<InterviewRecord> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 如果不是管理员，只返回用户自己的记录
            if (!user.isAdmin()) {
                predicates.add(cb.equal(root.get("user").get("userId"), user.getUserId()));
            }
            if (form.getCompanyName() != null && !form.getCompanyName().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("companyName")), "%" + form.getCompanyName().trim().toLowerCase() + "%"));
            }
            if (form.getStartTime() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("interviewTime"), form.getStartTime()));
            }
            if (form.getEndTime() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("interviewTime"), form.getEndTime()));
            }
            if (form.getPassed() != null) {
                predicates.add(cb.equal(root.get("passed"), form.getPassed()));
            }
            if (form.getInterviewRound() != null) {
                predicates.add(cb.equal(root.get("interviewRound"), form.getInterviewRound()));
            }
            if (form.getCompanyType() != null) {
                predicates.add(cb.equal(root.get("companyType"), form.getCompanyType()));
            }
            if (form.getSalaryRange() != null && !form.getSalaryRange().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("salaryRange")), "%" + form.getSalaryRange().trim().toLowerCase() + "%"));
            }
            if (form.getCompanyAddress() != null && !form.getCompanyAddress().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("companyAddress")), "%" + form.getCompanyAddress().trim().toLowerCase() + "%"));
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
        record.getQuestions().size();
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
        form.setInterviewTime(record.getInterviewTime());
        form.setCompanyName(record.getCompanyName());
        form.setInterviewRound(record.getInterviewRound());
        form.setCompanyType(record.getCompanyType());
        form.setSalaryRange(record.getSalaryRange());
        form.setCompanyAddress(record.getCompanyAddress());
        form.setPassed(record.getPassed());
        form.setRemark(record.getRemark());
        List<String> questions = new ArrayList<>();
        for (InterviewQuestion question : record.getQuestions()) {
            questions.add(question.getQuestionContent());
        }
        if (questions.isEmpty()) {
            questions.add("");
        }
        form.setQuestions(questions);
        form.setQuestionText(String.join("\n//\n", questions));
        return form;
    }

    private void applyForm(InterviewRecord record, InterviewRecordForm form) {
        record.setInterviewTime(form.getInterviewTime());
        record.setCompanyName(form.getCompanyName().trim());
        record.setInterviewRound(form.getInterviewRound());
        record.setCompanyType(form.getCompanyType());
        record.setSalaryRange(form.getSalaryRange().trim());
        record.setCompanyAddress(form.getCompanyAddress().trim());
        record.setPassed(form.getPassed());
        record.setRemark(form.getRemark());

        record.clearQuestions();
        for (String questionContent : form.getQuestions()) {
            String content = questionContent == null ? "" : questionContent;
            if (!content.trim().isEmpty()) {
                InterviewQuestion question = new InterviewQuestion();
                question.setQuestionContent(content);
                record.addQuestion(question);
            }
        }
        if (record.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("至少填写一条面试问题");
        }
    }

    private Sort buildSort(InterviewSearchForm form) {
        String sortBy = form.getSortBy();
        if (!"companyName".equals(sortBy) && !"salaryRange".equals(sortBy)) {
            sortBy = "interviewTime";
        }
        Sort.Direction direction = "asc".equalsIgnoreCase(form.getDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, sortBy);
    }
}
