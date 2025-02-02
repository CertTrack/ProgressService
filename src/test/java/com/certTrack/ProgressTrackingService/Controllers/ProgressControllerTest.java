package com.certTrack.ProgressTrackingService.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.certTrack.ProgressTrackingService.DTO.ResponseMessage;
import com.certTrack.ProgressTrackingService.Entity.Progress;
import com.fasterxml.jackson.databind.ObjectMapper;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProgressControllerTest {
	
	@Autowired
	MockMvc api;
	
	@Autowired
	ObjectMapper objectMapper;
	
	LocalDateTime dateTime = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	String lastUpdated = dateTime.format(formatter); 

	
	@Test
	@Order(1)
	public void NotAuthorizedUserCanNotSeeAnyEndpoint() throws Exception {
		api.perform(put("/progress/update?userId=1&courseId=7&progressPercentage=1")).andExpect(status().is4xxClientError());
		api.perform(get("/progress/get?userId=16&courseId=7")).andExpect(status().is4xxClientError());
		api.perform(get("/progress/all?userId=16")).andExpect(status().is4xxClientError());
		api.perform(delete("/progress/delete?userId=16&courseId=7")).andExpect(status().is4xxClientError());
	}
	
	
//	@WithMockUser
//	@Test
//	@Order(2)
//	public void AuthorizedCanUpdateProgresslol() throws Exception {
//		ResponseEntity<ResponseMessage> response = ResponseEntity.ok(new ResponseMessage("Progress updated successfully."));
//		String responseObject = objectMapper.writeValueAsString(response.getBody());
//		api.perform(put("/progress/update?userId=1&courseId=6&progressPercentage=10")
//			.contentType(MediaType.APPLICATION_JSON))
//			.andExpect(status().isOk())
//			.andExpect(content().json(responseObject));
//	}
	
	@WithMockUser
	@Test
	@Order(3)
	public void AuthorizedCanUpdateProgress() throws Exception {
		ResponseEntity<ResponseMessage> response = ResponseEntity.ok(new ResponseMessage("Progress updated successfully."));
		String responseObject = objectMapper.writeValueAsString(response.getBody());
		api.perform(put("/progress/update?userId=1&courseId=7&progressPercentage=10")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(responseObject));
	}
	
	
	
	@WithMockUser
	@Test
	@Order(4)
	public void AuthorizedCanSeeProgress() throws Exception {
		
		Progress progress = new Progress(1L, 7L, 100.0, lastUpdated);
		String responseObject = objectMapper.writeValueAsString(progress);
		api.perform(get("/progress/get?userId=1&courseId=7")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(responseObject));
	}
	
	@WithMockUser
	@Test
	@Order(5)
	public void AuthorizedCanSeeProgressOfAllCourses() throws Exception {
		List<Progress> progress = List.of(
				new Progress(1L, 6L, 100.0, lastUpdated),
				new Progress(1L, 7L, 100.0, lastUpdated));
		String responseObject = objectMapper.writeValueAsString(progress);
		api.perform(get("/progress/all?userId=1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(responseObject));
	}
	@WithMockUser
	@Test
	@Order(6)
	public void AuthorizedCanDeleteProgress() throws Exception {
		ResponseEntity<ResponseMessage> response = ResponseEntity.ok(new ResponseMessage("no progress by that id"));
		String responseObject = objectMapper.writeValueAsString(response.getBody());
		api.perform(delete("/progress/delete?userId=16&courseId=1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(content().json(responseObject));
	}
}
