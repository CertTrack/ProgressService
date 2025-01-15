package com.certTrack.ProgressTrackingService.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.certTrack.ProgressTrackingService.DTO.ResponseMessage;
import com.certTrack.ProgressTrackingService.Entity.Progress;
import com.certTrack.ProgressTrackingService.Repository.ProgressRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProgressService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ProgressRepository progressRepository;

	public void updateProgress(Long userId, Long courseId, int progressPercentage) {
		Progress progress = progressRepository.findByUserIdAndCourseId(userId, courseId)
				.orElseGet(() -> createNewProgress(userId, courseId));

		String query = "SELECT module FROM course WHERE id = ?";
		int modules = jdbcTemplate.queryForObject(query, Integer.class, courseId);
		double percentage = (progressPercentage / (double) modules) * 100;
		if (progress.getProgressPercentage() >= percentage) {
			return;
		}
		if (percentage == 100) {
			String url = "http://localhost:8085/notification/send?userId={userId}&message={message}&subject={subject}";
			String courseName = jdbcTemplate.queryForObject("SELECT name FROM course WHERE id = ?", String.class,
					courseId);
			String message = "Hello, we congratulate you on completing the course " + courseName + "!\n"
					+ "You will receive your certificate shortly!";
			String subject = "Completion of the " + courseName + " course";

			Map<String, String> params = new HashMap<>();
			params.put("userId", userId + "");
			params.put("message", message);
			params.put("subject", subject);
			try {
				ResponseEntity<ResponseMessage> response = restTemplate.postForEntity(url, null, ResponseMessage.class,
						params);
				if (response.getStatusCode() == HttpStatus.OK) {
					log.info("Response: " + response.getBody().getMessage());
				} else {
					log.error("Error: " + response.getStatusCode());
				}
			} catch (Exception e) {
				log.error("Exception occurred: " + e.getMessage());
			}
		}
		progress.setProgressPercentage(percentage);
		progressRepository.save(progress);
	}

	private Progress createNewProgress(Long userId, Long courseId) {
		Progress progress = new Progress();
		progress.setUserId(userId);
		progress.setCourseId(courseId);
		progress.setProgressPercentage(0.0);
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

	public ResponseEntity<ResponseMessage> deleteProgress(Long userId, Long courseId) {
		Optional<Progress> progress = progressRepository.findByUserIdAndCourseId(userId, courseId);
		if (progress.isPresent()) {
			progressRepository.delete(progress.get());
			return ResponseEntity.ok(new ResponseMessage("succesfully deleted progress"));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("no progress by that id"));
	}
}
