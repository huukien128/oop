abstract class Brick extends GameObject {
    private int hitPoints;
    private String type;

    public Brick(int x, int y, int width, int height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    public void takeHit() {
        hitPoints--;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }
}
