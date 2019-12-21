package com.joysupply.controller;

import org.springframework.stereotype.Controller;

@Controller 
public class MainController extends BaseController {
	//@GetMapping("/")
	public String main(){
		return "master";
	}
}
