package com.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/threads")
public class SchedulerController {

	/**
	 event-loop threads in action by executing all the tasks on a single thread.
	 */
	@GetMapping()
	public Flux<String> threads() {
		return Flux.range(1, 5)
				.map(i -> {
					System.out.println(Thread.currentThread().getName());
					return "Item " + i;
				});		
	}
	
	/**
	 * No parallelism here
	 * @return
	 */
	@GetMapping("/publish/sequential")
	public Flux<String> publishThreads() {
		return Flux.range(1, 15)
				.publishOn(Schedulers.boundedElastic())
				.map(i -> {
					System.out.println(Thread.currentThread().getName());
					return "Item " + i + " - ";
				});		
	}
	
	@GetMapping("/publish/parallel")
	public Flux<String> publishThreadsParallel() {
		return Flux.range(1, 15)
				.parallel()
				.runOn(Schedulers.boundedElastic())
				.map(i -> {
					System.out.println(Thread.currentThread().getName());
					return "Item " + i + " - ";
				})
				.sequential();		
		
	}
}