package com.certTrack.ProgressTrackingService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.certTrack.ProgressTrackingService.Entity.Progress;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Integer> {

	Optional<Progress> findByUserIdAndCourseId(Long userId, Long courseId);

	List<Progress> findByUserId(Long userId);

}