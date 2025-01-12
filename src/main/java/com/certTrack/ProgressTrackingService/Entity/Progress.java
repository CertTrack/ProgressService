package com.certTrack.ProgressTrackingService.Entity;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name = "progress")
public class Progress {


	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "progress_percentage", nullable = false)
    private Double progressPercentage;

    @Column(name = "last_updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private String lastUpdated;

    @PreUpdate
    @PrePersist
    public void onUpdate() throws ParseException {
    	LocalDateTime dateTime = LocalDateTime.now();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    	System.out.println("on update "+ dateTime.format(formatter));
    	this.lastUpdated = dateTime.format(formatter); 
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Double getProgressPercentage() {
		return progressPercentage;
	}

	public void setProgressPercentage(Double progressPercentage) {
		this.progressPercentage = progressPercentage;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Progress(Long userId, Long courseId, Double progressPercentage, String lastUpdated) {
		this.userId = userId;
		this.courseId = courseId;
		this.progressPercentage = progressPercentage;
		this.lastUpdated = lastUpdated;
	}

	public Progress() {	}
	@Override
	public String toString() {
		return "Progress [id=" + id + ", userId=" + userId + ", courseId=" + courseId + ", progressPercentage="
				+ progressPercentage + ", lastUpdated=" + lastUpdated + "]";
	}
}
