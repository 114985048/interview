package com.wu.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.wu.model.User;

@Entity
@Table(name = "interview_record")
public class InterviewRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interviewId;

    @Column(nullable = false)
    private LocalDateTime interviewTime;

    @Column(nullable = false, length = 200)
    private String companyName;

    @Column(nullable = false)
    private Integer interviewRound;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CompanyType companyType;

    @Column(nullable = false, length = 100)
    private String salaryRange;

    @Column(nullable = false, length = 300)
    private String companyAddress;

    @Column
    private Boolean passed;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "interviewRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InterviewQuestion> questions = new ArrayList<>();

    @Column(length = 255)
    private String fileName;

    @Column(length = 500)
    private String filePath;

    @Column(length = 100)
    private String contentType;

    @Column
    private Long fileSize;

    public void addQuestion(InterviewQuestion question) {
        question.setInterviewRecord(this);
        this.questions.add(question);
    }

    public void clearQuestions() {
        this.questions.clear();
    }

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

    public List<InterviewQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<InterviewQuestion> questions) {
        this.questions = questions;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
