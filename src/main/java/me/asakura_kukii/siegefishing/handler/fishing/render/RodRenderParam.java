package me.asakura_kukii.siegefishing.handler.fishing.render;

public class RodRenderParam {
    private double speed;
    private double inteval;
    private int color;
    private double size;

    // pressure
    private double pressure;

    public RodRenderParam(double speed, double interval, int color, double size, double pressure) {
        this.speed = speed;
        this.inteval = interval;
        this.color = color;
        this.size = size;
        this.pressure = pressure;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getInteval() {
        return inteval;
    }

    public void setInteval(double inteval) {
        this.inteval = inteval;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
