package me.asakura_kukii.siegefishing.handler.method.fishingbeta.stage;

public enum StageType {
    THROWN(new ThrowHandler()),
    FLOAT(new FloatHandler()),
    WAIT(new WaitHandler()),
    HOOKED(new HookedHandler()),
    FINISH(null);

    public StageHandler sH;
    StageType(StageHandler sH) {
        this.sH = sH;
    }
}
