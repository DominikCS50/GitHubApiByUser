# GitHubApiByUser

## Pre-requisites

* JDK 17

## Specification

As an api consumer, given username and header “Accept: application/json”, application lists all his github repositories, which are not forks. Information, that are given in the response, is:

Repository Name
Owner Login
For each branch it’s name and last commit sha

As an api consumer, given not existing github user, 404 response is received in such a format:

{
    “status”: ${responseCode}
    “Message”: ${whyHasItHappened
}

As an api consumer, given header “Accept: application/xml”, 406 response is received in such a format:

{
    “status”: ${responseCode}
    “Message”: ${whyHasItHappened
}

## Endpoint

http://localhost:8080/api/repositories/{username}

## Example

Request:
http://localhost:8080/api/repositories/DominikCS50

Response:
[
    {
        "name": "Capgemini-test",
        "owner": "DominikCS50",
        "fork": false,
        "branches": [
            {
                "name": "main",
                "lastCommitSha": null
            }
        ]
    },
    {
        "name": "CS50",
        "owner": "DominikCS50",
        "fork": false,
        "branches": [
            {
                "name": "main",
                "lastCommitSha": null
            }
        ]
    },
    {
        "name": "DatingApp",
        "owner": "DominikCS50",
        "fork": false,
        "branches": [
            {
                "name": "master",
                "lastCommitSha": null
            }
        ]
    },
    {
        "name": "GitHubApiByUser",
        "owner": "DominikCS50",
        "fork": false,
        "branches": [
            {
                "name": "main",
                "lastCommitSha": null
            }
        ]
    },
    {
        "name": "WAL-1247_task",
        "owner": "DominikCS50",
        "fork": false,
        "branches": [
            {
                "name": "main",
                "lastCommitSha": null
            }
        ]
    }
]
