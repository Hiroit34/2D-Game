package org.example.entity;

import org.example.GamePanel;
import org.example.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {

    private final GamePanel gp;
    private final KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public int hasKey = 0;

    private BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        loadPlayerImages();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "up";
    }

    private void loadPlayerImages() {
        try {
            up1 = loadImage("/player/boy_up_1.png");
            up2 = loadImage("/player/boy_up_2.png");
            down1 = loadImage("/player/boy_down_1.png");
            down2 = loadImage("/player/boy_down_2.png");
            left1 = loadImage("/player/boy_left_1.png");
            left2 = loadImage("/player/boy_left_2.png");
            right1 = loadImage("/player/boy_right_1.png");
            right2 = loadImage("/player/boy_right_2.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }

    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            collisionOn = false;
            gp.collisionChecker.checkTile(this);

            int objIndex = gp.collisionChecker.checkObjectCollision(this, true);
            pickUpObject(objIndex);

            if (!collisionOn) {
                movePlayer();
            }

            spriteCounter++;
            if (spriteCounter >= 13) {
                spriteNumber = (spriteNumber == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    private void movePlayer() {
        switch (direction) {
            case "up" -> worldY -= speed;
            case "down" -> worldY += speed;
            case "left" -> worldX -= speed;
            case "right" -> worldX += speed;
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String objectName = gp.obj[i].name;

            if ("Key".equals(objectName)) {
                gp.playSoundEffect(1);
                hasKey++;
                gp.obj[i] = null;
                gp.ui.showMessage("You got a KEY!");
            } else if ("Door".equals(objectName)) {
                gp.playSoundEffect(3);
                if (hasKey > 0) {
                    gp.obj[i] = null;
                    hasKey--;
                    gp.ui.showMessage("You OPENED a DOOR!");
                } else {
                    gp.ui.showMessage("You NEED a KEY!");
                }
            } else if ("Boots".equals(objectName)) {
                gp.playSoundEffect(2);
                speed += 2;
                gp.obj[i] = null;
                gp.ui.showMessage("You got BOOTS! RUUUN!");
            } else if ("Chest".equals(objectName)) {
                gp.ui.gameFinished = true;
                gp.pauseMusic();
                gp.playSoundEffect(4);
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getPlayerImageByDirection();
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    private BufferedImage getPlayerImageByDirection() {
        return switch (direction) {
            case "up" -> (spriteNumber == 1) ? up1 : up2;
            case "down" -> (spriteNumber == 1) ? down1 : down2;
            case "left" -> (spriteNumber == 1) ? left1 : left2;
            case "right" -> (spriteNumber == 1) ? right1 : right2;
            default -> null;
        };
    }
}
