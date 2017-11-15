package com.lofisoftware.vigilauntie.rex;

import com.badlogic.gdx.Gdx;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

/**
 *
 */
public class RexReader {

    private boolean disposed = false;
    private int layerCountOffset;
    private int layerCount = -1;
    private int[][] layerSizes = new int[4][2];
    private ByteBuffer deflated;
    private int filesize;

    private void checkDisposed() throws IOException {

        if (disposed) {
            throw new IOException("RexReader: stream already disposed.");
        }

    }

    public int loadXPFile(String file) throws IOException {

        FileInputStream fileIn = new FileInputStream(file);

        if (fileIn == null)
            return 0;

        return loadXPStream(fileIn);
    }

    public int loadXPStream(InputStream stream) throws IOException {
        checkDisposed();

        GZIPInputStream gZIPInputStream;

        gZIPInputStream = new GZIPInputStream(stream);
        //data = new DataInputStream(gZIPInputStream);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        int bytes_read = 0;
        byte[] buffer = new byte[1024];

        try {

            while ((bytes_read = gZIPInputStream.read(buffer,0,1024)) > 0) {
                bytes.write(buffer, 0, bytes_read);
                filesize += bytes_read;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Gdx.app.log("RexReader", "Bytes read: " + filesize);

        deflated = ByteBuffer.wrap(bytes.toByteArray());

        //Gdx.app.log("RexReader", "available: " + gZIPInputStream.available());

        if (getInt(deflated, 0) < 0)
            layerCountOffset = 4;

        layerCount = -1;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                layerSizes[i][j] = -1;
            }
        }

        return filesize;

    }

    public void dispose() throws IOException {
        disposed = true;
        //gZIPInputStream.close();

    }

    /*
    private int getInt(ByteBuffer in, int position) throws IOException {
        int value = 0;
        //byte[] buffer = new byte[4];

        if (position >= 0 && position < filesize) {

            value = in.getInt(position);
            //if (value == -1)
            //    return 0;
        }

        Gdx.app.log("RexReader", "getInt: " + value);

        return value;
    }
*/

    public int getLayerCount() throws IOException {
        checkDisposed();

        if (layerCount > 0)
        {
            return layerCount;
        }

        layerCount = getInt(deflated, layerCountOffset);
        return layerCount;
    }

    public int getLayerWidth(int layer) throws Exception, IOException {
        checkDisposed();

        if (layer < 0 || layer >= getLayerCount())
        {
            throw new Exception("getLayerWidth; Layer out of bounds");
        }

        if (layerSizes[layer][0] > 0)
        {
            return layerSizes[layer][0];
        }

        int offset = (layerCountOffset * 8 + 32 + layer * 64)/8;
        layerSizes[layer][0] = getInt(deflated, offset);

        return layerSizes[layer][0];
    }

    public int getLayerHeight(int layer) throws Exception, IOException {
        checkDisposed();

        if (layer < 0 || layer > getLayerCount())
        {
            throw new Exception("getLayerHeight: Layer out of bounds");
        }

        if (layerSizes[layer][1] > 0)
        {
            return layerSizes[layer][1];
        }

        int offset = (layerCountOffset * 8 + 32 + 32 + layer * 64)/8;
        layerSizes[layer][1] = getInt(deflated, offset);

        return layerSizes[layer][1];
    }

    public RexTileMap getTileMap() throws Exception, IOException {
        checkDisposed();
        int layers;
        int width;
        int height;

        layers = getLayerCount();
        width = getLayerWidth(0);
        height = getLayerHeight(0);

        RexTileMap map = new RexTileMap(width, height, layers);
        int offset = 16;

        for (int layer = 0; layer < layers; layer++){

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    map.getTileLayers()[layer].setTile(new RexTile(
                            getByte(deflated,offset),
                            getByte(deflated,offset+4),
                            getByte(deflated,offset+5),
                            getByte(deflated,offset+6),
                            getByte(deflated,offset+7),
                            getByte(deflated,offset+8),
                            getByte(deflated,offset+9)),
                            x,y);
                    offset+=10;
                }
            }
            offset = 16 + ((10 * width * height) + 8)*(layer+1);
        }

        return map;
    }

    public int getInt(ByteBuffer in, int position) throws IOException {
        int value = 0;
        byte[] buffer = new byte[4];

        if (position < 0)
        {
                throw new IOException("position must be > 0");
        }

        for (int b = 0; b < 4; b++) {
            buffer[b] = in.get(position + b);
        }

        //in.get(buffer, position, 4);
        value = (value << 8) + (buffer[3] & 0xFF);
        value = (value << 8) + (buffer[2] & 0xFF);
        value = (value << 8) + (buffer[1] & 0xFF);
        value = (value << 8) + (buffer[0] & 0xFF);

        //Gdx.app.log("RexReader","buffer[3]: " + buffer[3]);
        //Gdx.app.log("RexReader","buffer[2]: " + buffer[2]);
        //Gdx.app.log("RexReader", "buffer[1]: " + buffer[1]);
        //Gdx.app.log("RexReader","buffer[0]: " + buffer[0]);

        //Gdx.app.log("RexReader", "getInt: " + value);

        return value;
    }

    public byte getByte(ByteBuffer in, int position) throws IOException {

        byte[] buffer = {'0'};

        if (position < 0)
        {
            throw new IOException("position must be >= 0");
        }

        buffer[0] = in.get(position);

        //Gdx.app.log("RexReader", "getByte: " + RexTile.unsignedByteToInt(buffer[0]));

        return buffer[0];
    }


}
