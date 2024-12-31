package com.certTrack.ProgressTrackingService.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.certTrack.ProgressTrackingService.DTO.ResponseMessage;
import com.certTrack.ProgressTrackingService.Entity.Progress;
import com.certTrack.ProgressTrackingService.Repository.ProgressRepository;

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
		if(percentage == 100) {
	        String url = "http://localhost:8085/?userId={userId}&message={message}&subject={subject}";
	        String message = "Hello, this is a test message!";
	        String subject = "Test Subject";
	        try {
	            // Відправка GET-запиту
	            ResponseEntity<ResponseMessage> response = restTemplate.getForEntity(
	                    url,
	                    ResponseMessage.class,
	                    userId,
	                    message,
	                    subject
	            );
	            
	            // Перевірка статусу відповіді
	            if (response.getStatusCode() == HttpStatus.OK) {
	                System.out.println("Response: " + response.getBody().getMessage());
	            } else {
	                System.out.println("Error: " + response.getStatusCode());
	            }
	        } catch (Exception e) {
	            System.out.println("Exception occurred: " + e.getMessage());
	        }		
	    }
		progress.setProgressPercentage(percentage);
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
