class StrongBrick extends Brick {

    public StrongBrick(int x, int y, int width, int height) {
        super(x, y, width, height, 2, "Strong");
    }

    @Override
    public void update() { }

    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            g.setColor(Color.GRAY);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}
