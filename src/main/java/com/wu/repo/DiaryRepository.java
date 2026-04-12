package com.wu.repo;

import com.wu.model.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findByUserIdOrderByDiaryDateDesc(Long userId);

    Page<Diary> findByUserIdOrderByDiaryDateDesc(Long userId, Pageable pageable);

    Page<Diary> findAllByOrderByDiaryDateDesc(Pageable pageable);

    Optional<Diary> findByDiaryIdAndUserId(Long diaryId, Long userId);

    List<Diary> findByUserIdAndDiaryDateBetweenOrderByDiaryDateDesc(Long userId, Date startDate, Date endDate);

    @Query("SELECT d FROM Diary d WHERE d.userId = :userId AND " +
           "(LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.todaySummary) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY d.diaryDate DESC")
    List<Diary> searchByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);

    @Query("SELECT d FROM Diary d WHERE " +
           "(LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.todaySummary) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY d.diaryDate DESC")
    List<Diary> searchByKeyword(@Param("keyword") String keyword);

    boolean existsByUserIdAndDiaryDate(Long userId, Date diaryDate);

    Optional<Diary> findByUserIdAndDiaryDate(Long userId, Date diaryDate);
}
