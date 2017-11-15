package com.lofisoftware.vigilauntie.rex;

import com.badlogic.gdx.graphics.Color;

/**
 *
 */
public class RexTile {

    private byte characterCode;
    private byte foregroundRed;
    private byte foregroundGreen;
    private byte foregroundBlue;
    private byte backgroundRed;
    private byte backgroundGreen;
    private byte backgroundBlue;

    public RexTile() {

    }

    public RexTile(byte characterCode, byte foregroundRed, byte foregroundGreen, byte foregroundBlue, byte backgroundRed, byte backgroundGreen, byte backgroundBlue) {
        this.characterCode = characterCode;
        this.backgroundRed = backgroundRed;
        this.backgroundGreen = backgroundGreen;
        this.backgroundBlue = backgroundBlue;
        this.foregroundRed = foregroundRed;
        this.foregroundGreen = foregroundGreen;
        this.foregroundBlue = foregroundBlue;
    }

    public int getCharacterCode() {
        return unsignedByteToInt(characterCode);
    }

    public char getCharacter() {
        return (char) unsignedByteToInt(characterCode);
    }


    public void setCharacterCode(byte characterCode) {
        this.characterCode = characterCode;
    }

    public int getBackgroundRed() {
        return unsignedByteToInt(backgroundRed);
    }

    public void setBackgroundRed(byte backgroundRed) {
        this.backgroundRed = backgroundRed;
    }

    public int getBackgroundGreen() {
        return unsignedByteToInt(backgroundGreen);
    }

    public void setBackgroundGreen(byte backgroundGreen) {
        this.backgroundGreen = backgroundGreen;
    }

    public int getBackgroundBlue() {
        return unsignedByteToInt(backgroundBlue);
    }

    public void setBackgroundBlue(byte backgroundBlue) {
        this.backgroundBlue = backgroundBlue;
    }

    public int getForegroundRed() {
        return unsignedByteToInt(foregroundRed);
    }

    public void setForegroundRed(byte foregroundRed) {
        this.foregroundRed = foregroundRed;
    }

    public int getForegroundGreen() {
        return unsignedByteToInt(foregroundGreen);
    }

    public void setForegroundGreen(byte foregroundGreen) {
        this.foregroundGreen = foregroundGreen;
    }

    public int getForegroundBlue() {
        return unsignedByteToInt(foregroundBlue);
    }

    public void setForegroundBlue(byte foregroundBlue) {
        this.foregroundBlue = foregroundBlue;
    }

    public Color getForegroundColor() {

        return new Color(
                getForegroundRed()/255f, getForegroundGreen()/255f, getForegroundBlue()/255f, 1.0f
                        );
        /*
        return new Color(Color.toIntBits(
                getForegroundRed(), getForegroundGreen(), getForegroundBlue(), 255)
        );
        */
    }

    public Color getBackgroundColor() {
        return new Color(
                getBackgroundRed()/255f, getBackgroundGreen()/255f, getBackgroundBlue()/255f, 1.0f
        );
        /*
        return new Color(Color.toIntBits(
                getBackgroundRed(), getBackgroundGreen(), getBackgroundBlue(), 255)
        );
        */
    }

    public static int unsignedByteToInt(byte a)
    {
        int b = a & 0xFF;
        return b;
    }

}
