package me.dragosghinea.model;

import lombok.Getter;
import lombok.Setter;
import me.dragosghinea.model.abstracts.Reward;

import java.util.List;

@Getter
@Setter
public class BundleReward {

    private List<Reward> rewards;
}
