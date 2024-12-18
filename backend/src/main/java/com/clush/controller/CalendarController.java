package com.clush.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clush.entity.CalendarEntity;
import com.clush.service.CalendarService;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

	@Autowired
	private CalendarService calendarService;
	
	@GetMapping("/{yearMonth}")
	public ResponseEntity<List<CalendarEntity>> getCalendar(@PathVariable("yearMonth") String yearMonth){
		return calendarService.getCalendarService(yearMonth);
	}
}
