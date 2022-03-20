package ca.unb.mobiledev.reflexrevolution.utils;

import ca.unb.mobiledev.reflexrevolution.R;

public enum Difficulty {
    NOVICE(R.string.novice, R.string.description_novice),
    INTERMEDIATE(R.string.intermediate, R.string.description_intermediate),
    MASTER(R.string.master, R.string.description_master);

    private final int name;
    private final int description;
    Difficulty(int name, int description) {
        this.name = name;
        this.description = description;
    }
    public int getName() {
        return this.name;
    }
    public int getDescription() {
        return this.description;
    }
}
