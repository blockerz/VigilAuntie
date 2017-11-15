package com.lofisoftware.vigilauntie.rex;

/**
 *
 */
public class RexTileLayer {

    private RexTile[] tiles;
    private int width;
    private int height;

    public RexTileLayer (int width, int height) {
        tiles = new RexTile[width * height];
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public RexTile getTile(int x, int y) {
        if (isValid(x, y))
            return tiles[x+(y*width)];
        else
            return null;
    }

    public boolean isValid(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;
        return true;
    }

    public void setTile(RexTile tile, int x, int y) {
        if (isValid(x,y))
            this.tiles[x + (y*width)] = tile;
    }

    public void setTiles(RexTile[] tiles) {
        this.tiles = tiles;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
