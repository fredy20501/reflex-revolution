package ca.unb.mobiledev.reflexrevolution.utils;

import ca.unb.mobiledev.reflexrevolution.R;

public enum GameMode {
    CLASSIC(R.string.classic, R.string.description_classic, false),
    TACTILE(R.string.tactile, R.string.description_tactile, false),
    SWIPE(R.string.swipe, R.string.description_swipe, false),
    KINETIC(R.string.kinetic, R.string.description_kinetic, false),
    KEYBOARD(R.string.keyboard, R.string.description_keyboard, false),
    REVOLUTION(R.string.revolution, R.string.description_revolution, false),
    TAP_PRACTICE(0, 0, true),
    SWIPE_PRACTICE(0, 0, true),
    SHAKE_PRACTICE(0, 0, true),
    JUMP_PRACTICE(0, 0, true),
    ROTATION_PRACTICE(0, 0, true),
    FREEZE_PRACTICE(0, 0, true),
    TYPE_PRACTICE(0, 0, true),
    DIAL_PRACTICE(0, 0, true);

    private final int name;
    private final int description;
    private final boolean isPractice;
    GameMode(int name, int description, boolean isPractice) {
        this.name = name;
        this.description = description;
        this.isPractice = isPractice;
    }
    public int getName() {
        return this.name;
    }
    public int getDescription() {
        return this.description;
    }
    public boolean isPractice() { return this.isPractice; }
}
