package com.atipera.task.service;

import com.atipera.task.controller.GitHubController;
import com.atipera.task.model.Branch;
import com.atipera.task.model.Repository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GitHubService {
    private final WebClient webClient;

    public GitHubService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getGitHubAccessToken())
                .filter(logRequest())
                .build();
    }
    private String getGitHubAccessToken() {
        String accessToken = System.getenv("GITHUB_TOKEN");
        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("GitHub access token not found. Please set the GITHUB_TOKEN environment variable.");
        }
        return accessToken;
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientRequest);
        });
    }

    public List<Repository> getRepositories(String username) {
        Flux<Repository> repositoryFlux = webClient.get()
                .uri("/users/{username}/repos?type=all", username)
                .retrieve()
                .bodyToFlux(Repository.class)
                .filter(repository -> !repository.isFork());

        return repositoryFlux.collectList()
                .onErrorMap(throwable -> {
                    if (throwable.getMessage().contains("404 Not Found")) {
                        throw new GitHubController.UserNotFoundException("The requested GitHub user does not exist.");
                    }
                    return throwable;
                })
                .block();
    }

    public List<Branch> getBranches(String owner, String repoName) {
        return webClient.get()
                .uri("/repos/{owner}/{repoName}/branches", owner, repoName)
                .retrieve()
                .bodyToFlux(Branch.class)
                .collectList()
                .block();
    }
}
