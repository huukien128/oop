class BonusBrick extends Brick {

    private boolean powerUpSpawned = false;

    public BonusBrick(int x, int y, int width, int height) {
        super(x, y, width, height, 3, "Bonus");
    }

    @Override
    public void update() { }

    @Override
    public void render(Graphics g) {
        if (!isDestroyed()) {
            g.setColor(Color.YELLOW);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public void takeHit() {
        super.takeHit();

        if (isDestroyed() && !powerUpSpawned) {
            spawnPowerUp();
            powerUpSpawned = true;
        }
    }

    // Sinh vật phẩm
    private void spawnPowerUp() {
        // Giả định bạn có class PowerUp (và hệ thống quản lý chúng)
        // thêm các chức năng có được khi nhận vật phẩm
    }
}
