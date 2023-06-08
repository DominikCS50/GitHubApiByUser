package com.atipera.task.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Repository {
    private String name;
    @JsonSerialize(using = OwnerSerializer.class)

    private Owner owner;
    private boolean fork;
    private List<Branch> branches;

    public boolean isFork() {
        return fork;
    }
    @Data
    public static class Owner {
        private String login;
    }
}

