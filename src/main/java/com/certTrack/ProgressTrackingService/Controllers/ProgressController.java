package com.certTrack.ProgressTrackingService.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.certTrack.ProgressTrackingService.DTO.ResponseMessage;
import com.certTrack.ProgressTrackingService.Entity.Progress;
import com.certTrack.ProgressTrackingService.Security.UserPrincipal;
import com.certTrack.ProgressTrackingService.Service.ProgressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @PutMapping("/update")
    public ResponseEntity<ResponseMessage> updateProgress(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam Long courseId,
            @RequestParam int progressPercentage){
        progressService.updateProgress(user.getUserId(), courseId, progressPercentage);
        return ResponseEntity.ok(new ResponseMessage("Progress updated successfully."));
    }

    @GetMapping("/get")
    public Progress getProgress(
    		@AuthenticationPrincipal UserPrincipal user,
            @RequestParam Long courseId) {
        return progressService.getProgressByUserIdAndCourseId(user.getUserId(), courseId);
    }
    
    @GetMapping("/all")
    public List<Progress> getAllUserProgress(
    		@AuthenticationPrincipal UserPrincipal user) {
        return progressService.getProgressByUserId(user.getUserId());
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseMessage> clearProgressByUserIdAndCourseId(
            @RequestParam Long userId,
            @RequestParam Long courseId) {
    	return progressService.deleteProgress(userId, courseId);
    }
}
