package ca.unb.mobiledev.reflexrevolution.utils;

import ca.unb.mobiledev.reflexrevolution.R;

public enum GameMode {
    CLASSIC(R.string.classic, R.string.description_classic, false),
    TACTILE(R.string.tactile, R.string.description_tactile, false),
    SWIPE(R.string.swipe, R.string.description_swipe, false),
    KINETIC(R.string.kinetic, R.string.description_kinetic, false),
    KEYBOARD(R.string.keyboard, R.string.description_keyboard, false),
    REVOLUTION(R.string.revolution, R.string.description_revolution, false),
    CUSTOM(0,0, false),
    TAP_TUTORIAL(0, 0, true),
    SWIPE_TUTORIAL(0, 0, true),
    SHAKE_TUTORIAL(0, 0, true),
    JUMP_TUTORIAL(0, 0, true),
    ROTATION_TUTORIAL(0, 0, true),
    FREEZE_TUTORIAL(0, 0, true),
    TYPE_TUTORIAL(0, 0, true),
    DIAL_TUTORIAL(0, 0, true);

    private final int name;
    private final int description;
    private final boolean isTutorial;
    GameMode(int name, int description, boolean isTutorial) {
        this.name = name;
        this.description = description;
        this.isTutorial = isTutorial;
    }
    public int getName() {
        return this.name;
    }
    public int getDescription() {
        return this.description;
    }
    public boolean isTutorial() { return this.isTutorial; }
}
