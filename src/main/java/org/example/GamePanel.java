package org.example;

import org.example.entity.Player;
import org.example.object.SuperObject;
import org.example.tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // Constants
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize;
    public final int screenHeight = maxScreenRow * tileSize;
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    final int FPS = 60;

    // Game Components
    final TileManager tileManager = new TileManager(this);
    final KeyHandler keyH = new KeyHandler();
    public final UI ui = new UI(this);
    public final Player player = new Player(this, this.keyH);
    public final CollisionChecker collisionChecker = new CollisionChecker(this);
    public final SuperObject[] obj = new SuperObject[10];
    public final AssetSetter assetSetter = new AssetSetter(this);
    final Sound sound = new Sound();

    // Game Thread
    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public void setupGame() {
        assetSetter.setObject();
        playMusic(0);
    }

    public void startGameThread() {
        this.requestFocus();
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileManager.draw(g2);

        for (SuperObject superObject : obj) {
            if (superObject != null) {
                superObject.draw(g2, this);
            }
        }

        player.draw(g2);
        ui.draw(g2);
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void pauseMusic() {
        sound.stop();
    }

    public void playSoundEffect(int i) {
        Sound se = new Sound();
        se.setFile(i);
        se.play();
    }
}
