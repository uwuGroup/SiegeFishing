package me.asakura_kukii.siegefishing.handler.player.input;

public enum InputKeyType {
    MOVE(false, false),
    SPRINT(false, false),
    SNEAK(false, false),
    AIRBORNE(false, false),

    ITEM_LEFT_CLICK(false, true),
    ITEM_RIGHT_CLICK(true, true),
    ITEM_SWAP(false, true),
    ITEM_INITIATE(false, true),
    ITEM_FINALIZE(false, true),
    ITEM_DROP(false, true)
    ;

    public boolean checkHold;
    public boolean doInput;

    InputKeyType(boolean checkHold, boolean doInput) {
        this.checkHold = checkHold;
        this.doInput = doInput;
    }
}
