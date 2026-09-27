package com.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mono")
public class MonoController {
	
	@GetMapping
	public Mono<String> hello() {
		return Mono.just("hello reactive world from Mono");
	}

}
