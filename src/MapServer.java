public class MapServer extends Map {
    /**
     * Constructs a new server-side game map.
     * Also serves as a "main" method for spawning food and computer players.
     * @param imagePath the image to use as the background
     */
    public MapServer(String imagePath) {
        super(imagePath);

        for (int i = 0; i < 30; i++) {
            Food food = new Food();
            addFood(food);
        }

        for (int i = 0; i < 10; i++) {
            Computer tigerAI = new Computer();
            addTiger(tigerAI);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long lastUpdate = System.currentTimeMillis();
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        System.out.printf(
                            "Map update thread interrupted: %s\n", e
                        );
                    }

                    for (Computer tiger : getComputers()) {
                        tiger.update(System.currentTimeMillis() - lastUpdate);
                    }
                }
            }
        }).start();
    }
    
}
