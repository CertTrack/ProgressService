package com.certTrack.ProgressTrackingService.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.certTrack.ProgressTrackingService.Entity.Progress;
import com.certTrack.ProgressTrackingService.Repository.ProgressRepository;

@Service
public class ProgressService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ProgressRepository progressRepository;

	public void updateProgress(Long userId, Long courseId, Double progressPercentage) {
		Progress progress = progressRepository.findByUserIdAndCourseId(userId, courseId)
				.orElseGet(() -> createNewProgress(userId, courseId));

		progress.setProgressPercentage(progressPercentage);
		progress.setLastUpdated(new Date());

		progressRepository.save(progress);
	}

	private Progress createNewProgress(Long userId, Long courseId) {
		Progress progress = new Progress();
		progress.setUserId(userId);
		progress.setCourseId(courseId);
		progress.setProgressPercentage(0.0);
		progress.setLastUpdated(new Date());
		return progress;
	}

	public Progress getProgressByUserIdAndCourseId(Long userId, Long courseId) {
		Progress progress = progressRepository.findByUserIdAndCourseId(userId, courseId).orElse(null);
		return progress;
	}
	public List<Progress> getProgressByUserId(Long userId) {
		List<Progress> progress = progressRepository.findByUserId(userId);
		return progress;
	}

	public void deleteProgress(Long userId, Long courseId) {
		Progress progress = progressRepository.findByUserIdAndCourseId(userId, courseId).orElse(null);
		progressRepository.delete(progress);
	}
}
