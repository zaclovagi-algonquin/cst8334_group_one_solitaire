package com.cst8334_group_one_solitaire.beans;

import java.util.ArrayList;
import java.util.List;

public class GameSpace {

    public int x, y;
    public int xDim, yDim;

    public GameSpace(int x, int y, int xDim, int yDim) {
        this.x = x;
        this.y = y;
        this.xDim = x + xDim;
        this.yDim = y + yDim;
    }
    public GameSpace(GameSpace gameSpace) {
        this.x = gameSpace.x;
        this.y = gameSpace.y;
        this.xDim = gameSpace.x + gameSpace.xDim;
        this.yDim = gameSpace.y + gameSpace.yDim;
    }

    public GameSpace(int x, int y) {
        this.x = x;
        this.y = y;
        this.xDim = x;
        this.yDim = y;
    }

    public boolean isInArea(int xPos, int yPos) {
        return xPos >= x && xPos <= xDim && yPos >= y && yPos <= yDim;
    }

    public int width() {
        return xDim - x;
    }

    public int height() {
        return yDim - y;
    }

    public void recalculateBounds(int occurrences) {
        this.yDim = y + Card.HEIGHT + (40 * occurrences);
    }

}
