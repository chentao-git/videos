package com.imooc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HelloWorldController {
	
	@RequestMapping(value = "/hello",method = GET)
	public String Hello() {
		return "Hello Spring Boot~";
	}
	
}
