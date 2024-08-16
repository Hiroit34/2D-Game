package org.example;

import org.example.object.Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {

    private final GamePanel gp;
    private final Font arial_40, arial_80Bold;
    private final BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    private int messageCounter = 0;
    public boolean gameFinished = false;
    private double playTime = 0;
    private final DecimalFormat df = new DecimalFormat("#0.00");

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80Bold = new Font("Arial", Font.BOLD, 80);
        Key key = new Key();
        keyImage = key.image;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    private void drawCenteredText(Graphics2D g2, String text, int y, Font font, Color color) {
        g2.setFont(font);
        g2.setColor(color);
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - textLength / 2;
        g2.drawString(text, x, y);
    }

    public void draw(Graphics2D g2) {
        if (gameFinished) {
            drawCenteredText(g2, "You found the treasure!", gp.screenHeight / 2 - (gp.tileSize * 3), arial_40, Color.white);
            drawCenteredText(g2, "Your TIME is " + df.format(playTime), gp.screenHeight / 2 + (gp.tileSize * 4), arial_40, Color.white);
            drawCenteredText(g2, "CONGRATULATIONS", gp.screenHeight / 2 + (gp.tileSize * 2), arial_80Bold, Color.yellow);
            gp.gameThread = null;
        } else {
            g2.setFont(arial_40);
            g2.setColor(Color.white);
            g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, 74, 65);

            playTime += 1.0 / 60;
            g2.drawString("TIME: " + df.format(playTime), gp.tileSize * 11, 65);

            if (messageOn) {
                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);

                messageCounter++;
                if (messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }
    }
}