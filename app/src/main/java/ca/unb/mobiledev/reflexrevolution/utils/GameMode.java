package ca.unb.mobiledev.reflexrevolution.utils;

import ca.unb.mobiledev.reflexrevolution.R;

public enum GameMode {
    CLASSIC(0, R.id.classic, R.string.classic, R.string.description_classic, false),
    TACTILE(1, R.id.tactile, R.string.tactile, R.string.description_tactile, false),
    SWIPE(2, R.id.swipe, R.string.swipe, R.string.description_swipe, false),
    KINETIC(3, R.id.kinetic, R.string.kinetic, R.string.description_kinetic, false),
    KEYBOARD(4, R.id.keyboard, R.string.keyboard, R.string.description_keyboard, false),
    REVOLUTION(5, R.id.revolution, R.string.revolution, R.string.description_revolution, false),
    TAP_PRACTICE(6, 0, 0, 0, true),
    SWIPE_PRACTICE(7, 0, 0, 0, true),
    SHAKE_PRACTICE(8, 0, 0, 0, true),
    JUMP_PRACTICE(9, 0, 0, 0, true),
    TURN_PRACTICE(10, 0, 0, 0, true),
    TILT_PRACTICE(11, 0, 0, 0, true),
    TWIST_PRACTICE(12, 0, 0, 0, true),
    FREEZE_PRACTICE(13, 0, 0, 0, true),
    TYPE_PRACTICE(14, 0, 0, 0, true),
    DIAL_PRACTICE(15, 0, 0, 0, true);

    private final int id, chipId, name, description;
    private final boolean isPractice;
    GameMode(int id, int chipId, int name, int description, boolean isPractice) {
        this.id = id;
        this.chipId = chipId;
        this.name = name;
        this.description = description;
        this.isPractice = isPractice;
    }
    public int getId() {
        return this.id;
    }
    public int getChipId() {
        return this.chipId;
    }
    public int getName() {
        return this.name;
    }
    public int getDescription() {
        return this.description;
    }
    public boolean isPractice() { return this.isPractice; }
}
