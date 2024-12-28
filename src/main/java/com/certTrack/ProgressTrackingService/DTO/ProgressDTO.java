package com.certTrack.ProgressTrackingService.DTO;

public class ProgressDTO {
	
	private Double progressPercentage;

	
	public Double getProgressPercentage() {
		return progressPercentage;
	}

	public void setProgressPercentage(Double progressPercentage) {
		this.progressPercentage = progressPercentage;
	}

	public ProgressDTO(Double progressPercentage) {
		super();
		this.progressPercentage = progressPercentage;
	}
	public ProgressDTO() {
	}
	
}
