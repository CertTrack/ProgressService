package com.certTrack.ProgressTrackingService.Entity;

import java.util.Date;

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
    private Date lastUpdated;

    @PreUpdate
    @PrePersist
    protected void onUpdate() {
        this.lastUpdated = new Date();
        System.out.println("onUpdate");
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

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Progress(Long id, Long userId, Long courseId, Double progressPercentage, Date lastUpdated) {
		super();
		this.id = id;
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
