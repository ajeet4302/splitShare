package com.splitwise.dto;

import jakarta.validation.constraints.NotBlank;

public class GroupRequest {

    @NotBlank(message = "Group name is required")
    private String groupName;

    private String description;

    public GroupRequest() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}