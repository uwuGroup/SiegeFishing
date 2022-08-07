package me.asakura_kukii.siegefishing.handler.fishing.render;

import me.asakura_kukii.siegefishing.SiegeFishing;
import me.asakura_kukii.siegefishing.handler.fishing.FishingSession;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class FishingRenderer {
    private ThreadRenderer threadRenderer;
    private HookRenderer hookRenderer;
    private FishRenderer fishRenderer;

    List<BasicRenderer> renderers = new ArrayList<>();

    private FishingSession session;

    public FishingRenderer(FishingSession session){
        this.session = session;
    }


    private boolean active = false;

    public void startRender() {
        if (active){
            checkAndStopRender();
        }
        this.active = true;
        threadRenderer = new ThreadRenderer(session);
        hookRenderer = new HookRenderer(session);
        fishRenderer = new FishRenderer(session);
        renderers.add(threadRenderer);
        renderers.add(hookRenderer);
        renderers.add(fishRenderer);

        renderers.forEach(this::runTask);
    }

    private void runTask(BukkitRunnable threadRenderer) {
        threadRenderer.runTaskTimer(SiegeFishing.pluginInstance, 0, 0);
    }

    public void stopRender() {
        this.active = false;
        checkAndStopRender();
    }

    public void onHitWater(){
        renderers.forEach(BasicRenderer::onHitWater);
    }

    public void onHooked(){
        renderers.forEach(BasicRenderer::onHooked);
    }

    public void onFinish(){
        renderers.forEach(BasicRenderer::onFinish);
    }


    private void checkAndStopRender() {
        this.renderers.forEach(renderer -> {
            if (renderer != null) {
                renderer.stop();
            }
        });
        threadRenderer = null;
        hookRenderer = null;
        fishRenderer = null;
        this.renderers.clear();
    }

    public void onFail() {
        renderers.forEach(BasicRenderer::onFail);
    }
}
