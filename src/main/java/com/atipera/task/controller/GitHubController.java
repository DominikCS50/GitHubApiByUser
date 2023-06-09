package com.atipera.task.controller;

import com.atipera.task.dto.ErrorDto;
import com.atipera.task.model.Branch;
import com.atipera.task.model.Repository;
import com.atipera.task.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GitHubController {
    private final GitHubService gitHubService;

    @Autowired
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping(value = "/repositories/{username}", produces = {"application/json", "application/xml"})
    public List<Repository> getUserRepositories(@PathVariable String username) {
        List<Repository> repositories = gitHubService.getRepositories(username);
        repositories.forEach(repository -> {
            List<Branch> branches = gitHubService.getBranches(repository.getOwner().getLogin(), repository.getName());
            repository.setBranches(branches);
        });
        return repositories;
    }

    @ExceptionHandler(UserNotFoundException .class)
    public ResponseEntity<Object> handleNonExistingUser() {
        ErrorDto errorResponse = new ErrorDto();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage("The requested GitHub user does not exist.");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<Object> handleNotAcceptable() {
        ErrorDto errorResponse = new ErrorDto();
        errorResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        errorResponse.setMessage("The requested media type is not supported.");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).headers(headers).body(errorResponse);
    }
}