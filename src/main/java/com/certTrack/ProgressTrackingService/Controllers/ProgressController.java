package com.certTrack.ProgressTrackingService.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.certTrack.ProgressTrackingService.Entity.Progress;
import com.certTrack.ProgressTrackingService.Service.ProgressService;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    
    
    
    @PutMapping("/")
    public ResponseEntity<String> updateProgress(
            @RequestParam Long userId,
            @RequestParam Long courseId,
            @RequestParam int progressPercentage){
        progressService.updateProgress(userId, courseId, progressPercentage);
        return ResponseEntity.ok("Progress updated successfully.");
    }

    @GetMapping("/")
    public Progress getProgress(
            @RequestParam Long userId,
            @RequestParam Long courseId) {
        return progressService.getProgressByUserIdAndCourseId(userId, courseId);
    }
    
    @GetMapping("/all")
    public List<Progress> getAllUserProgress(
            @RequestParam Long userId) {
        return progressService.getProgressByUserId(userId);
    }
    

    
    
    @DeleteMapping("/")
    public ResponseEntity<String> clearProgressByUserIdAndCourseId(
            @RequestParam Long userId,
            @RequestParam Long courseId) {
        progressService.deleteProgress(userId, courseId);
        return ResponseEntity.ok("Progress successfully deleted.");
    }
}