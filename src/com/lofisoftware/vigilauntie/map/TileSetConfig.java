package com.lofisoftware.vigilauntie.map;


import com.badlogic.gdx.utils.Array;

public class TileSetConfig {

    private String tilesetName;
    private int tileWidth;
    private int tileHeight;
    private Array<TileConfig> tileConfigs;
    private MapTileSet.TileType tileType;

    public String getTilesetName() {
        return tilesetName;
    }

    public void setTilesetName(String tilesetName) {
        this.tilesetName = tilesetName;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public Array<TileConfig> getTileConfigs() {
        return tileConfigs;
    }

    public void setTileConfigs(Array<TileConfig> tileConfigs) {
        this.tileConfigs = tileConfigs;
    }

    static public class TileConfig {
        private int tileWidth = 32;
        private int tileHeight = 16;
        private String texturePath;
        private int row;
        private int column;

        public TileConfig() {

        }

        public int getTileWidth() {
            return tileWidth;
        }

        public void setTileWidth(int tileWidth) {
            this.tileWidth = tileWidth;
        }

        public int getTileHeight() {
            return tileHeight;
        }

        public void setTileHeight(int tileHeight) {
            this.tileHeight = tileHeight;
        }

        public String getTexturePath() {
            return texturePath;
        }

        public void setTexturePath(String texturePath) {
            this.texturePath = texturePath;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }
    }

}
