package com.example.sanisidropharmacy;

import java.util.List;

public class RewardsResponse {
    private boolean success;
    private List<Reward> rewards;
    public boolean isSuccess() { return success; }
    public List<Reward> getRewards() { return rewards; }

    public static class Reward {
        public int id;
        public String title;
        public String description;
        public int points_required;
    }
}
