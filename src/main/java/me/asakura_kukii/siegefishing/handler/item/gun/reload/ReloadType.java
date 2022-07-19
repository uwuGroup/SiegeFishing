package me.asakura_kukii.siegefishing.handler.item.gun.reload;

public enum ReloadType {
    CLIP(new ClipHandler());

    public Reload r;
    ReloadType(Reload r) {
        this.r = r;
    }
}
