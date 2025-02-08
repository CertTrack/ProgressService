package com.certTrack.ProgressTrackingService.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.certTrack.ProgressTrackingService.Configuration.NotificationProducer;
import com.certTrack.ProgressTrackingService.DTO.ResponseMessage;
import com.certTrack.ProgressTrackingService.Entity.Progress;
import com.certTrack.ProgressTrackingService.Repository.ProgressRepository;
import com.certTrack.ProgressTrackingService.Security.TokenGenerator;

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

	@Autowired
	private NotificationProducer notificationProducer;

	@Autowired
	TokenGenerator tokenGenerator;

	/**
	 * Updates the progress of a user in a specific course and handles certification and notification processes.
	 *
	 * @param userId             the ID of the user
	 * @param courseId           the ID of the course
	 * @param progressPercentage the percentage of progress made by the user
	 * <p>
	 * This method retrieves or creates the user's progress in the specified course. It then calculates the new 
	 * progress percentage and updates it if it is greater than the current percentage. If the progress reaches 100%, 
	 * the method saves the certification and sends an email notification to the user.
	 * 
	 * Important: The parameter 'type' in the notification parameters map is used to determine the notification type:
	 * - If type = 1: It indicates a completion notification.
	 * - If type = 2: It indicates a different type of notification (e.g., progress update).
	 * </p>
	 */
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
			// save certification
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			String token = tokenGenerator.generateServiceToken(Integer.valueOf(userId + ""));
			System.out.println(token);
			headers.setBearerAuth(token);

			HttpEntity<String> entity = new HttpEntity<>(null, headers);

			String url = "http://localhost:8083/certifications/upload?userId={userId}&courseId={courseId}";
			ResponseEntity<ResponseMessage> response = restTemplate.exchange(url, HttpMethod.POST, entity,
					ResponseMessage.class, userId, courseId);
			if (response.getStatusCode() == HttpStatus.OK) {
				log.info("Response: " + response.getBody().getMessage());
			} else {
				log.error("Error: " + response.getStatusCode());
			}

			// send email notification

			Map<String, String> params = new HashMap<>();
			params.put("userId", userId + "");
			params.put("courseId", courseId + "");
			params.put("type", "1");
			// params.put("message", message);
			// params.put("subject", subject);
			/*
			 * try { ResponseEntity<ResponseMessage> response =
			 * restTemplate.postForEntity(url, null, ResponseMessage.class, params); if
			 * (response.getStatusCode() == HttpStatus.OK) { log.info("Response: " +
			 * response.getBody().getMessage()); } else { log.error("Error: " +
			 * response.getStatusCode()); } } catch (Exception e) {
			 * log.error("Exception occurred: " + e.getMessage()); }
			 */
			notificationProducer.sendNotification(params);
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
