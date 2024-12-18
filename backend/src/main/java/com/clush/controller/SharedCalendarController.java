package com.clush.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clush.dto.SharedCalendarDto;
import com.clush.dto.ToDoDto;
import com.clush.entity.SharedCalendarEntity;
import com.clush.service.SharedCalendarService;
import com.clush.util.AESUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("shared")
public class SharedCalendarController {
	
	@Autowired
	private SharedCalendarService sharedCalendarService;
	
	@GetMapping
	public ResponseEntity<List<SharedCalendarDto>> getSharedCalendar(){
		return sharedCalendarService.getSharedCalendarService();
	}
	
	@GetMapping("{id}")
	public ResponseEntity<SharedCalendarDto> getSharedToDos(@PathVariable("id") long id){
		return sharedCalendarService.getSharedToDosService(id);
	}
	
	@PostMapping
	public ResponseEntity<Void> createSharedCalendar(@RequestBody String name){
		return sharedCalendarService.createSharedCalendarService(name);
	}
	
	@PostMapping("{id}")
	public ResponseEntity<Void> createSharedToDo(@RequestBody ToDoDto toDoDto,@PathVariable("id") long id){
		return sharedCalendarService.createSharedToDoService(id,toDoDto);
	}
	
	@GetMapping("invite")
	public ResponseEntity<Void> shareCalendar(@RequestParam("accept") String accept, HttpServletRequest request) throws Exception{
		return sharedCalendarService.shareCalendarService(Long.valueOf(AESUtil.decrypt(accept)),request);
	}
}
