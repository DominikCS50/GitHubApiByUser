package com.atipera.task.controller;

import com.atipera.task.model.Branch;
import com.atipera.task.model.Repository;
import com.atipera.task.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GitHubController {
    private final GitHubService gitHubService;

    @Autowired
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/repositories/{username}")
    public List<Repository> getUserRepositories(@PathVariable String username) {
        List<Repository> repositories = gitHubService.getRepositories(username);
        repositories.forEach(repository -> {
            List<Branch> branches = gitHubService.getBranches(repository.getOwner().getLogin(), repository.getName());
            repository.setBranches(branches);
        });
        return repositories;
    }
}