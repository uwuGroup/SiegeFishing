package me.asakura_kukii.siegefishing.handler.nonitem.potential;

public enum PotentialType {
    WEAPON(5,10,0.05,0.25,0.5,true, "wp");

    public String identifier;
    public int row;
    public int column;
    public double visionPercent;
    public double minFillPercent;
    public double maxFillPercent;
    public boolean lAsMainPotential;
    PotentialType(int row, int column, double visionPercent, double minFillPercent, double maxFillPercent, boolean lAsMainPotential, String identifier) {
        this.identifier = identifier;
        this.row = row;
        this.column = column;
        this.visionPercent = visionPercent;
        this.minFillPercent = minFillPercent;
        this.maxFillPercent = maxFillPercent;
        this.lAsMainPotential = lAsMainPotential;
    };
}
