package com.wu.service;

import com.wu.model.Diary;
import com.wu.repo.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    @Autowired
    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    @Transactional(readOnly = true)
    public List<Diary> findByUserId(Long userId) {
        return diaryRepository.findByUserIdOrderByDiaryDateDesc(userId);
    }

    @Transactional(readOnly = true)
    public Page<Diary> findByUserIdWithPage(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "diaryDate"));
        return diaryRepository.findByUserIdOrderByDiaryDateDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Diary> findAllWithPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "diaryDate"));
        return diaryRepository.findAllByOrderByDiaryDateDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Diary> findByIdAndUserId(Long diaryId, Long userId) {
        return diaryRepository.findByDiaryIdAndUserId(diaryId, userId);
    }

    @Transactional(readOnly = true)
    public Optional<Diary> findByUserIdAndDate(Long userId, Date date) {
        return diaryRepository.findByUserIdAndDiaryDate(userId, date);
    }

    @Transactional
    public Diary save(Diary diary) {
        return diaryRepository.save(diary);
    }

    @Transactional
    public void delete(Long diaryId, Long userId) {
        Diary diary = diaryRepository.findByDiaryIdAndUserId(diaryId, userId)
                .orElseThrow(() -> new IllegalArgumentException("日记不存在或无权限删除"));
        diaryRepository.delete(diary);
    }

    @Transactional(readOnly = true)
    public List<Diary> searchByKeyword(Long userId, String keyword) {
        return diaryRepository.searchByUserIdAndKeyword(userId, keyword);
    }

    @Transactional(readOnly = true)
    public List<Diary> searchAllByKeyword(String keyword) {
        return diaryRepository.searchByKeyword(keyword);
    }

    @Transactional(readOnly = true)
    public Optional<Diary> findById(Long diaryId) {
        return diaryRepository.findById(diaryId);
    }

    @Transactional(readOnly = true)
    public List<Diary> findByDateRange(Long userId, Date startDate, Date endDate) {
        return diaryRepository.findByUserIdAndDiaryDateBetweenOrderByDiaryDateDesc(userId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public boolean existsByDate(Long userId, Date date) {
        return diaryRepository.existsByUserIdAndDiaryDate(userId, date);
    }

    @Transactional(readOnly = true)
    public long countByUserId(Long userId) {
        return diaryRepository.findByUserIdOrderByDiaryDateDesc(userId).size();
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return diaryRepository.count();
    }

    @Transactional
    public void deleteById(Long diaryId) {
        diaryRepository.deleteById(diaryId);
    }
}
