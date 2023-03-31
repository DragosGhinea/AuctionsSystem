package me.dragosghinea.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserAuctions {

    private Set<UUID> auctions = new HashSet<>();
}
