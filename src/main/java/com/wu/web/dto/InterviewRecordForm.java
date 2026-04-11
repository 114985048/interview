package com.wu.web.dto;

import com.wu.model.CompanyType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InterviewRecordForm {

    private Long interviewId;

    @NotNull(message = "面试时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime interviewTime;

    @NotBlank(message = "公司名称不能为空")
    private String companyName;

    @NotNull(message = "面试轮次不能为空")
    @Min(value = 1, message = "面试轮次必须为正整数")
    private Integer interviewRound;

    @NotNull(message = "公司类型不能为空")
    private CompanyType companyType;

    @NotBlank(message = "薪资范围不能为空")
    private String salaryRange;

    @NotBlank(message = "公司地址不能为空")
    private String companyAddress;

    @NotNull(message = "面试结果不能为空")
    private Boolean passed;

    private String remark;

    private List<String> questions = new ArrayList<>();
    private String questionText;

    public Long getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(Long interviewId) {
        this.interviewId = interviewId;
    }

    public LocalDateTime getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(LocalDateTime interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getInterviewRound() {
        return interviewRound;
    }

    public void setInterviewRound(Integer interviewRound) {
        this.interviewRound = interviewRound;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
