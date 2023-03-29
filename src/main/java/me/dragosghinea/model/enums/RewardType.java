package me.dragosghinea.model.enums;

public enum RewardType {
    SINGLE("Single", "A singular item reward."),
    MULTIPLE("Multiple", "A reward that consists of multiple items."),
    BUNDLE("Bundle", "A reward that consists of more rewards");

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
