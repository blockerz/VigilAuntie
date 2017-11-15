package com.lofisoftware.vigilauntie.rex;

/**
 *
 */
public class RexTileMap {

    private RexTileLayer[] tileLayers;
    private int width;
    private int height;

    public RexTileLayer [] getTileLayers() {
        return tileLayers;
    }

    public RexTileLayer getTileLayer(int l) {
        if (l <= 0 || l > tileLayers.length)
            return null;
        return tileLayers[l-1];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLayerCount() {
        if (tileLayers == null)
            return 0;
        return tileLayers.length;
    }

    public RexTileMap(int width, int height, int layers) {
        tileLayers = new RexTileLayer[layers];
        this.width = width;
        this.height = height;
        for (int i = 0; i < layers; i++) {
            tileLayers[i] = new RexTileLayer(width, height);
        }
    }
}
