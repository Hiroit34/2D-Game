package org.example;

import org.example.entity.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNumber1, tileNumber2;

        // Aggiunta di controlli per evitare ArrayIndexOutOfBoundsException
        if (entityLeftCol < 0) entityLeftCol = 0;
        if (entityRightCol >= gp.tileManager.mapTileNumber.length) entityRightCol = gp.tileManager.mapTileNumber.length - 1;
        if (entityTopRow < 0) entityTopRow = 0;
        if (entityBottomRow >= gp.tileManager.mapTileNumber[0].length) entityBottomRow = gp.tileManager.mapTileNumber[0].length - 1;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                if (entityTopRow >= 0) { // Verifica che l'indice non sia fuori dai limiti
                    tileNumber1 = gp.tileManager.mapTileNumber[entityLeftCol][entityTopRow];
                    tileNumber2 = gp.tileManager.mapTileNumber[entityRightCol][entityTopRow];
                    if (gp.tileManager.tile[tileNumber1].collision || gp.tileManager.tile[tileNumber2].collision) {
                        entity.collisionOn = true;
                    }
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                if (entityBottomRow < gp.tileManager.mapTileNumber[0].length) { // Verifica che l'indice non sia fuori dai limiti
                    tileNumber1 = gp.tileManager.mapTileNumber[entityLeftCol][entityBottomRow];
                    tileNumber2 = gp.tileManager.mapTileNumber[entityRightCol][entityBottomRow];
                    if (gp.tileManager.tile[tileNumber1].collision || gp.tileManager.tile[tileNumber2].collision) {
                        entity.collisionOn = true;
                    }
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                if (entityLeftCol >= 0) { // Verifica che l'indice non sia fuori dai limiti
                    tileNumber1 = gp.tileManager.mapTileNumber[entityLeftCol][entityTopRow];
                    tileNumber2 = gp.tileManager.mapTileNumber[entityLeftCol][entityBottomRow];
                    if (gp.tileManager.tile[tileNumber1].collision || gp.tileManager.tile[tileNumber2].collision) {
                        entity.collisionOn = true;
                    }
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                if (entityRightCol < gp.tileManager.mapTileNumber.length) { // Verifica che l'indice non sia fuori dai limiti
                    tileNumber1 = gp.tileManager.mapTileNumber[entityRightCol][entityTopRow];
                    tileNumber2 = gp.tileManager.mapTileNumber[entityRightCol][entityBottomRow];
                    if (gp.tileManager.tile[tileNumber1].collision || gp.tileManager.tile[tileNumber2].collision) {
                        entity.collisionOn = true;
                    }
                }
                break;
        }
    }

    public int checkObjectCollision(Entity entity, boolean player) {

        int index = 999;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {

                // Salva le coordinate originali di solidArea
                int entitySolidAreaX = entity.solidArea.x;
                int entitySolidAreaY = entity.solidArea.y;
                int objSolidAreaX = gp.obj[i].solidArea.x;
                int objSolidAreaY = gp.obj[i].solidArea.y;

                // Modifica temporaneamente le coordinate di solidArea per il controllo della collisione
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                }

                // Ripristina le coordinate originali di solidArea
                entity.solidArea.x = entitySolidAreaX;
                entity.solidArea.y = entitySolidAreaY;
                gp.obj[i].solidArea.x = objSolidAreaX;
                gp.obj[i].solidArea.y = objSolidAreaY;
            }
        }

        return index;
    }
}
