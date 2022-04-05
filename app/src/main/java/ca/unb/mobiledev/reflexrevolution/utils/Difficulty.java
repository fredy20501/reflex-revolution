package ca.unb.mobiledev.reflexrevolution.utils;

import ca.unb.mobiledev.reflexrevolution.R;

public enum Difficulty {
    NOVICE(1600, 105, R.string.novice, R.string.description_novice),
    INTERMEDIATE(1300, 70, R.string.intermediate, R.string.description_intermediate),
    MASTER(1000, 35, R.string.master, R.string.description_master);

    private final int extraDuration, maxScore, name, description;
    Difficulty(int extraDuration, int maxScore, int name, int description) {
        this.extraDuration = extraDuration;
        this.maxScore = maxScore;
        this.name = name;
        this.description = description;
    }
    public int getExtraDuration() {
        return this.extraDuration;
    }
    public int getMaxScore() {
        return this.maxScore;
    }
    public int getName() {
        return this.name;
    }
    public int getDescription() {
        return this.description;
    }
}
