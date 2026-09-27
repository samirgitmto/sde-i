package com.gql.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.server.webmvc.GraphiQlHandler;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GraphiQlConfig {

	/**
	 * Replaces Boot 3.3's built-in GraphiQL (unpinned CDN scripts that often stick on "Loading...")
	 * with a page that uses pinned React/GraphiQL versions.
	 */
	@Bean
	@Order(0)
	RouterFunction<ServerResponse> customGraphiQlRouter() {
		GraphiQlHandler handler = new GraphiQlHandler(
				"/graphql",
				"/graphql",
				new ClassPathResource("graphql/graphiql.html"));
		return RouterFunctions.route()
				.GET("/graphiql", request -> handler.handleRequest(request))
				.build();
	}
}
