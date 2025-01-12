package com.certTrack.ProgressTrackingService.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.certTrack.ProgressTrackingService.DTO.ResponseMessage;
import com.certTrack.ProgressTrackingService.Entity.Progress;
import com.certTrack.ProgressTrackingService.Service.ProgressService;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    
    
    
    @PutMapping("/update")
    public ResponseEntity<ResponseMessage> updateProgress(
            @RequestParam Long userId,
            @RequestParam Long courseId,
            @RequestParam int progressPercentage){
        progressService.updateProgress(userId, courseId, progressPercentage);
        return ResponseEntity.ok(new ResponseMessage("Progress updated successfully."));
    }

    @GetMapping("/get")
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
    

    
    
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseMessage> clearProgressByUserIdAndCourseId(
            @RequestParam Long userId,
            @RequestParam Long courseId) {
    	return progressService.deleteProgress(userId, courseId);
    }
}