package ca.unb.mobiledev.reflexrevolution.utils;

import ca.unb.mobiledev.reflexrevolution.R;

public enum Difficulty {
    NOVICE(0, R.id.novice, 1600, 105, R.string.novice, R.string.description_novice),
    INTERMEDIATE(1, R.id.intermediate, 1300, 70, R.string.intermediate, R.string.description_intermediate),
    MASTER(2, R.id.master, 1000, 35, R.string.master, R.string.description_master);

    private final int id, chipId, extraDuration, maxScore, name, description;
    Difficulty(int id, int chipId, int extraDuration, int maxScore, int name, int description) {
        this.id = id;
        this.chipId = chipId;
        this.extraDuration = extraDuration;
        this.maxScore = maxScore;
        this.name = name;
        this.description = description;
    }
    public int getId() {
        return this.id;
    }
    public int getChipId() {
        return this.chipId;
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
