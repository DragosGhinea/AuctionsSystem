package me.dragosghinea.model.enums;

public enum RewardType {
    SINGLE("Single", "A singular item reward."),
    MULTI("Multiple", "A reward that consists of multiple items."),
    BUNDLE("Bundle", "A reward that consists of more rewards."),
    UNKNOWN("Unknown", "No specification on this reward.");

    private String title;
    private String description;

    RewardType(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
