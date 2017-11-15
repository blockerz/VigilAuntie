package com.lofisoftware.vigilauntie.map;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.lofisoftware.vigilauntie.Utility;

public class MapTileSet extends TiledMapTileSet {

    public enum TileType {
        GROUND,
        BACKGROUND
    }

    private int tilesWide, tilesHigh;

    public MapTileSet(String textureName, int width, int height) {
        Utility.loadTextureAsset(textureName);
        Texture texture = Utility.getTextureAsset(textureName);

        TextureRegion[][] textureFrames = TextureRegion.split(texture, width, height);

        int id = 0;

        if (textureFrames != null) {

            tilesWide = textureFrames[0].length;
            tilesHigh = textureFrames.length;

            for (int row = 0; row < tilesHigh; row++) {
                for (int col = 0; col < tilesWide; col++) {
                    putTile(id++, new MapTile(textureFrames[row][col]));
                }
            }

        }
    }

    public int getTilesWide() {
        return tilesWide;
    }

    public int getTilesHigh() {
        return tilesHigh;
    }

}
