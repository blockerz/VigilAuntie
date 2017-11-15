package com.lofisoftware.vigilauntie.map;

import com.badlogic.gdx.math.MathUtils;

import squidpony.squidmath.Coord;

public enum Direction {

    UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0), UP_LEFT(-1, 1), UP_RIGHT(1, 1), DOWN_LEFT(-1, -1), DOWN_RIGHT(1, -1), NONE(0, 0);
    /**
     * An array which holds only the four cardinal directions.
     */
    public static final Direction[] CARDINALS = {UP, DOWN, LEFT, RIGHT};
    /**
     * An array which holds only the four diagonal directions.
     */
    public static final Direction[] DIAGONALS = {UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};
    /**
     * An array which holds all eight OUTWARDS directions.
     */
    public static final Direction[] OUTWARDS = {UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT};
    /**
     * The x coordinate difference for this direction.
     */
    public final int deltaX;
    /**
     * The y coordinate difference for this direction.
     */
    public final int deltaY;

    Direction(int x, int y) {
        deltaX = x;
        deltaY = y;
    }

    static public Direction getRandomNext() {
        return Direction.values()[MathUtils.random(Direction.values().length - 1)];
    }

    public Direction getOpposite() {
        if( this == LEFT){
            return RIGHT;
        }else if( this == RIGHT){
            return LEFT;
        }else if( this == UP){
            return DOWN;
        }else{
            return UP;
        }
    }

//    public Coord getNewPosition(Coord pos) {
//        if( this == LEFT){
//            return Coord.get(pos.getX() -1, pos.getY());
//        }else if( this == RIGHT){
//            return Coord.get(pos.getX() +1, pos.getY());
//        }else if( this == UP){
//            return Coord.get(pos.getX(), pos.getY() -1);
//        }else if( this == DOWN){
//            return Coord.get(pos.getX(), pos.getY() +1);
//        }else {
//            return Coord.get(pos.getX(), pos.getY());
//        }
//    }

    /**
     * Returns the direction that most closely matches the input.
     *
     * This can be used to get the primary magnitude intercardinal direction
     * from an origin point to an event point, such as a mouse click on a grid.
     *
     * If the point given is exactly on a boundary between directions then the
     * direction clockwise is returned.
     *
     * @param x
     * @param y
     * @return
     */
    public static Direction getDirection(int x, int y) {
        if (x == 0 && y == 0) {
            return NONE;
        }

        double angle = Math.atan2(y, x);
        double degree = Math.toDegrees(angle);
        degree += 450;//rotate to all positive and 0 is up
        degree %= 360;//normalize
        if (degree < 22.5) {
            return DOWN;
        } else if (degree < 67.5) {
            return DOWN_RIGHT;
        } else if (degree < 112.5) {
            return RIGHT;
        } else if (degree < 157.5) {
            return UP_RIGHT;
        } else if (degree < 202.5) {
            return UP;
        } else if (degree < 247.5) {
            return UP_LEFT;
        } else if (degree < 292.5) {
            return LEFT;
        } else if (degree < 337.5) {
            return DOWN_LEFT;
        } else {
            return UP;
        }
    }

    /**
     * Returns the direction that most closely matches the input.
     *
     * This can be used to get the primary magnitude cardinal direction from an
     * origin point to an event point, such as a mouse click on a grid.
     *
     * If the point given is directly diagonal then the direction clockwise is
     * returned.
     *
     * @param x
     * @param y
     * @return
     */
    public static Direction getCardinalDirection(int x, int y) {
        if (x == 0 && y == 0) {
            return NONE;
        }

        int absx = Math.abs(x);

        if (y > absx) {
            return UP;
        }

        int absy = Math.abs(y);

        if (absy > absx) {
            return DOWN;
        }

        if (x > 0) {
            if (-y == x) {//on diagonal
                return DOWN;
            }
            return RIGHT;
        }

        if (y < 0 && y == x)
            return DOWN;

        if (y == x) {//on diagonal
            return UP;
        }
        return LEFT;

    }

    /**
     * @param from
     *            The starting point.
     * @param to
     *            The desired point to reach.
     * @return The direction to follow to go from {@code from} to {@code to}. It
     *         can be cardinal or diagonal.
     */
    public static Direction toGoTo(Coord from, Coord to) {
        return getDirection(to.x - from.x, to.y - from.y);
    }

    /**
     * Returns the Direction one step clockwise including diagonals.
     *
     * If considering only Cardinal directions, calling this twice will get the
     * next clockwise cardinal direction.
     *
     * @return
     */
    public Direction clockwiseCardinal() {
        switch (this) {
            case UP:
                return RIGHT;
            case DOWN:
                return LEFT;
            case LEFT:
                return UP;
            case RIGHT:
                return DOWN;
            case NONE:
            default:
                return NONE;
        }
    }

    /**
     * Returns the Direction one step clockwise including diagonals.
     *
     * If considering only Cardinal directions, calling this twice will get the
     * next clockwise cardinal direction.
     *
     * @return
     */
    public Direction clockwise() {
        switch (this) {
            case UP:
                return UP_RIGHT;
            case DOWN:
                return DOWN_LEFT;
            case LEFT:
                return UP_LEFT;
            case RIGHT:
                return DOWN_RIGHT;
            case UP_LEFT:
                return UP;
            case UP_RIGHT:
                return RIGHT;
            case DOWN_LEFT:
                return LEFT;
            case DOWN_RIGHT:
                return DOWN;
            case NONE:
            default:
                return NONE;
        }
    }

    /**
     * Returns the Direction one step counterclockwise including diagonals.
     *
     * If considering only Cardinal directions, calling this twice will get the
     * next counterclockwise cardinal direction.
     *
     * @return
     */
    public Direction counterClockwise() {
        switch (this) {
            case UP:
                return UP_LEFT;
            case DOWN:
                return DOWN_RIGHT;
            case LEFT:
                return DOWN_LEFT;
            case RIGHT:
                return UP_RIGHT;
            case UP_LEFT:
                return LEFT;
            case UP_RIGHT:
                return UP;
            case DOWN_LEFT:
                return DOWN;
            case DOWN_RIGHT:
                return RIGHT;
            case NONE:
            default:
                return NONE;
        }
    }

    /**
     * Returns the direction directly opposite of this one.
     *
     * @return
     */
    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case UP_LEFT:
                return DOWN_RIGHT;
            case UP_RIGHT:
                return DOWN_LEFT;
            case DOWN_LEFT:
                return UP_RIGHT;
            case DOWN_RIGHT:
                return UP_LEFT;
            case NONE:
            default:
                return NONE;
        }
    }
}
