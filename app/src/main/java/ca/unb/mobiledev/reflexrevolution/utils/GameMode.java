package ca.unb.mobiledev.reflexrevolution.utils;

import ca.unb.mobiledev.reflexrevolution.R;

public enum GameMode {
    CLASSIC(R.string.classic, R.string.description_classic),
    TACTILE(R.string.tactile, R.string.description_tactile),
    SWIPE(R.string.swipe, R.string.description_swipe),
    KINETIC(R.string.kinetic, R.string.description_kinetic),
    KEYBOARD(R.string.keyboard, R.string.description_keyboard),
    REVOLUTION(R.string.revolution, R.string.description_revolution),
    CUSTOM(0,0);

    private final int name;
    private final int description;
    GameMode(int name, int description) {
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
