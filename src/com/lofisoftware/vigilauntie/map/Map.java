package com.lofisoftware.vigilauntie.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.lofisoftware.vigilauntie.entity.Entity;

import java.util.Comparator;
import java.util.Iterator;

import squidpony.squidmath.Coord;

import static com.lofisoftware.vigilauntie.Constants.*;

public class Map extends Group {

    private static final String TAG = Map.class.getSimpleName();

    private int width, height, visibleTileWidth, visibleGroundTileHeight, visibleBackgroundTileHeight;
    private int[][] pathMapLeft, pathMapRight;
    MapTileSet groundTileSet, backgroundTileSet;
    TiledMap tiledMap;
    private Array<Entity> mapEntities;
    private Coord player;
//    private OrthogonalTiledMapRenderer mapRenderer;
//    private OrthographicCamera camera;

    Map(int tilesWide, int tilesHigh) {
        this.width = tilesWide;
        this.height = tilesHigh;

        visibleTileWidth = width/2;
        visibleGroundTileHeight = tilesHigh/2;
        visibleBackgroundTileHeight = MAP_HEIGHT_CELLS - visibleGroundTileHeight;

        pathMapLeft = new int[width] [height];
        pathMapRight = new int[width] [height];

        mapEntities = new Array<Entity>();
        player = Coord.get(0,0);

    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void generateGroundTileLayer(String tileSetFileName) {

        groundTileSet = new MapTileSet(tileSetFileName, CELL_WIDTH, CELL_HEIGHT);
        tiledMap = new TiledMap();

        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer ground = new TiledMapTileLayer(visibleTileWidth+VISIBLE_WIDTH_PADDING,VISIBLE_TILE_HEIGHT,CELL_WIDTH, CELL_HEIGHT);
        ground.setName("GROUND");

        //MapLayer collision = new MapLayer();

//        0   1   2   3
//        4   5   6   7
//        8   9  10  11
//        12 13  14  15
//        16 17  18  19
//        20 21  22  23
//        24 25  26  27
        int row = 0,column = 0;

        for (int x = 0; x < visibleTileWidth+VISIBLE_WIDTH_PADDING; x++) {
            for (int y = VISIBLE_TILE_HEIGHT-1; y >= 0 ; y--) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

                if (row == 2 && MathUtils.random() > 0.8)
                    row = 3;
                cell.setTile(groundTileSet.getTile(row + column));
                if (row == 3)
                    row--;
                ground.setCell(x, y, cell);
                column += 4;
                if (column % 24 == 0)
                    column = 0;
            }
            row++;
            if (row % 3 == 0)
                row = 0;
        }
        layers.add(ground);
    }

    public void generateMapTileLayer(String groundTileSetFileName, String backgroundTileSetFileName) {

        groundTileSet = new MapTileSet(groundTileSetFileName, CELL_WIDTH, CELL_HEIGHT);
        backgroundTileSet = new MapTileSet(backgroundTileSetFileName, CELL_WIDTH, CELL_HEIGHT);
        tiledMap = new TiledMap();

        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer ground = new TiledMapTileLayer(visibleTileWidth+VISIBLE_WIDTH_PADDING,MAP_HEIGHT_CELLS,CELL_WIDTH, CELL_HEIGHT);
        ground.setName("GROUND");

        //MapLayer collision = new MapLayer();

//        0   1
//        2   3
//        4   5
//        6   7
//        8   9
//       10  11

//        0   1   2   3
//        4   5   6   7
//        8   9  10  11
//        12 13  14  15
//        16 17  18  19
//        20 21  22  23
//        24 25  26  27
        int row = 0,column = 0;

        int tileOffset = (backgroundTileSet.getTilesHigh()-visibleBackgroundTileHeight) * backgroundTileSet.getTilesWide();

        for (int x = 0; x < visibleTileWidth+VISIBLE_WIDTH_PADDING; x++) {
            for (int y = MAP_HEIGHT_CELLS-1; y >= visibleGroundTileHeight ; y--) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

                cell.setTile(backgroundTileSet.getTile(row + column + tileOffset));

                ground.setCell(x, y, cell);
                column += backgroundTileSet.getTilesWide();
                if (column % (backgroundTileSet.getTilesWide()*visibleBackgroundTileHeight) == 0)
                    column = 0;
            }
            row++;
            if (row % backgroundTileSet.getTilesWide() == 0)
                row = 0;
        }

        row = column = 0;
        tileOffset = 0;

        for (int x = 0; x < visibleTileWidth+VISIBLE_WIDTH_PADDING; x++) {
            for (int y = visibleGroundTileHeight-1; y >= 0 ; y--) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

                // final ground tile column is random for variety
                if (row == groundTileSet.getTilesWide()-2 && MathUtils.random() < GROUND_TILE_RANDOM_COLUMN_CHANCE)
                    row = groundTileSet.getTilesWide()-1;

                cell.setTile(groundTileSet.getTile(row + column));

                if (row == groundTileSet.getTilesWide()-1)
                    row--;

                ground.setCell(x, y, cell);
                column += groundTileSet.getTilesWide();
                if (column % (groundTileSet.getTilesWide()*visibleGroundTileHeight) == 0)
                    column = 0;
            }
            row++;
            if (row % (groundTileSet.getTilesWide()-1) == 0)
                row = 0;
        }

        layers.add(ground);
    }

    public int getMapWidth() {
        return width;
    }

    public int getMapHeight() {
        return height;
    }

//    public OrthographicCamera getCamera() {
//        return camera;
//    }
//
//    public void setCamera(OrthographicCamera camera) {
//        this.camera = camera;
//        mapRenderer = new OrthogonalTiledMapRenderer(getTiledMap());
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
//        //camera.update();
//        //mapRenderer.setView(this.computeTransform(), 0, 0, this.getWidth(), this.getHeight());
//        //mapRenderer.setView(this.getCamera().projection,this.getX(), this.getY(),this.getWidth(),this.getHeight());
//        Gdx.gl.glViewport(0,);
//        this.getCamera().update();
//        mapRenderer.setView(this.getCamera());
//        mapRenderer.render();
//
//    }

    public void update (float delta) {
        mapEntities.sort(new EntityZOrderComparator());
        int z = 0;
        for(Iterator<Entity> iter = mapEntities.iterator(); iter.hasNext();) {
            Entity entity = iter.next();
            entity.update(delta);
            entity.getImage().setZIndex(z++);
        }

    }

    public class EntityZOrderComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity arg0, Entity arg1) {
            if (arg0.getCurrentPosition().getY() > arg1.getCurrentPosition().getY()) {
                return -1;
            } else if (arg0.getCurrentPosition().getY() == arg1.getCurrentPosition().getY()) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public boolean isCollision(Coord position) {

        if (!isValidPlayerPosition(position.getX(), position.getY()))
            return true;

        if (getEntity(position.getX(),position.getY()) != null)
            return true;

        return false;
    }

    public boolean isValidPlayerPosition(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;
        return true;
    }

//    public boolean isValidTilePosition(int x, int y) {
//        if (x < 0 || x >= width || y < 0 || y >= height)
//            return false;
//        return true;
//    }

    public Entity getEntity (int x, int y) {
        if (!isValidPlayerPosition(x, y))
            return null;

        Array<Entity> entities = new Array<>();
        entities.addAll(mapEntities);

        for(Iterator<Entity> iter = entities.iterator(); iter.hasNext();) {
            Entity entity = iter.next();
            if (entity.getCurrentPosition() == Coord.get(x,y)) {
                return entity;
            }
        }

        return null;
    }

    public boolean setEntity(int x, int y, Entity entity) {

        if (!isValidPlayerPosition(x, y))
            return false;

        if (entity != null) {
            entity.setCurrentPosition(Coord.get(x,y));
            mapEntities.add(entity);

            addActor(entity.getImage());
            return true;
        }

        return false;
    }

    public boolean removeEntity(Entity entity) {

        if (entity != null) {
            removeActor(entity.getImage());
            mapEntities.removeValue(entity, true);
            return true;
        }

        return false;
    }

    public Array<Entity> getMapEntities() {
        return mapEntities;
    }

    public void dispose() {

    }

//    public int floodFillMapFromPoint(int region, int x, int y, boolean updateRoomMap) {
//        int size = 1;
//        int furthestRoomID = 0;
//
//        for (int x2 = 0; x2 < width; x2++) {
//            for (int y2 = 0; y2 < height; y2++) {
//                pathMap[x2][y2] = 0;
//            }
//        }
//
//        ArrayList<Point> open = new ArrayList<Point>();
//        open.add(new Point(x,y));
//        pathMap[x] [y] = region;
//
//        while (!open.isEmpty()){
//            Point p = open.remove(0);
//
//            for (Point neighbor : p.neighbors4()){
//                if (pathMap.getTile(neighbor.x(), neighbor.y(), Map.FILL_LAYER) > 0
//                        || !isPassable(neighbor.x(), neighbor.y()))
//                    continue;
//
//                size++;
//                pathMap.setTile(neighbor.x(), neighbor.y(),Map.FILL_LAYER, region);
//                open.add(neighbor);
//
//                // fill room list while we are here
//                int id = pathMap.getTile(neighbor.x(), neighbor.y(),Map.ID_LAYER);
//                if (updateRoomMap && !rooms.containsKey(id) && pathMap.getRoomByID(id) != null) {
//                    rooms.put(id, pathMap.getRoomByID(id).getRoomDimension());
//                    Gdx.app.log("ZoneBuilder:fillRegion", "Added room id:" + id);
//                    furthestRoomID = id;
//                }
//            }
//        }
//        return furthestRoomID;
//    }

    boolean isPassable (int x, int y) {
        if (!isValidPlayerPosition(x,y))
            return false;
        if (player.getX() == x && player.getY() == y)
            return false;
        return true;
    }

    public void calculatePath (int x, int y) {
        player = Coord.get(x,y);
        calculatePath(pathMapLeft, x-1, y);
        calculatePath(pathMapRight, x+1, y);
    }

    public int calculatePath (int [][] pathMap, int x, int y) {

        final int startingValue = -1;

        int maxDistance = 0, currentDistance = 0, breakLoop = 0;

        for (int x2 = 0; x2 < width; x2++) {
            for (int y2 = 0; y2 < height; y2++) {
                pathMap[x2] [y2] = startingValue;
            }
        }

        if (!isValidPlayerPosition(x,y))
            return -1;

        Array<Point> frontier = new Array<Point>();

        Point currentPoint = new Point(x,y);

        frontier.add(currentPoint);
        pathMap[currentPoint.x()] [currentPoint.y()] = currentDistance;

        // hack to push path toward east and west only - dont do this normally
        //if (isValidPlayerPosition(x,y+1))
        //    pathMap[x][y+1] = 3;
        //if (isValidPlayerPosition(x,y-1))
        //    pathMap[x][y-1] = 3;
        // end hack

        //Gdx.app.log("calculatePaths","Current Point: " + currentPoint.x() + ", " + currentPoint.y() + " frontier size: " + frontier.size);

        while (frontier.size > 0 && breakLoop++ < 100000) {
            currentPoint = frontier.removeIndex(0);
            currentDistance = pathMap[currentPoint.x()] [currentPoint.y()];
            //Gdx.app.log("calculatePaths","Current Point: " + currentPoint.x() + ", " + currentPoint.y() + " frontier size: " + frontier.size);
            for (Point neighbor : currentPoint.neighbors4()) {
                if ( isValidPlayerPosition(neighbor.x(), neighbor.y()) && pathMap[neighbor.x()] [neighbor.y()] != startingValue )
                    continue;

                if (!isPassable(neighbor.x(), neighbor.y()))
                    continue;

                //Gdx.app.log("calculatePaths","Neighbor Point: " + neighbor.x() + ", " + neighbor.y() + " frontier size: " + frontier.size);

                pathMap[neighbor.x()] [neighbor.y()] = 1 + currentDistance;
                frontier.add(neighbor);

                if (1 + currentDistance > maxDistance)
                    maxDistance = currentDistance + 1;

            }
        }



        printMap(pathMap);
        return maxDistance;
    }

    public int getDistanceToPath(int x, int y) {
        if (!isValidPlayerPosition(x,y))
            return -1;
        return Math.min(pathMapLeft[x][y], pathMapRight[x][y]);
    }

    public int[][] getClosestPath(int x, int y) {
        if (!isValidPlayerPosition(x,y))
            return null;
        return (pathMapLeft[x][y] < pathMapRight[x][y])? pathMapLeft : pathMapRight;
    }

    public int[][] getFurthestPath(int x, int y) {
        if (!isValidPlayerPosition(x,y))
            return null;
        return (pathMapLeft[x][y] > pathMapRight[x][y])? pathMapLeft : pathMapRight;
    }

    public Array<Coord> getNextPathPoints(int x, int y) {

        int[][] pathMap;

        if (getEntity(player.getX()+1,player.getY()) != null)
            pathMap = pathMapLeft;
        else if (getEntity(player.getX()-1,player.getY()) != null)
            pathMap = pathMapRight;
        else
            pathMap = getClosestPath(x,y);

        //if (pathMap[x][y] < 4 && getEntity())

        Array<Coord> points = getNextPathPoints(pathMap,x,y);

//        if (points != null && points.size > 0)
//            return points;
//
//        pathMap = getFurthestPath(x, y);
//
//        points = getNextPathPoints(pathMap,x,y);

        return points;

    }

    public Array<Coord> getNextPathPoints(int[][] pathMap, int x, int y) {

        if (!isValidPlayerPosition(x,y))
            return null;

        Array<Coord> points = new Array<>();

        Point currentPosition = new Point(x,y);
        Coord nextPosition1 = null;

        if (pathMap == null)
            return null;

        int currentScore = pathMap[x][y];

        if (currentScore < 0)
            return null;

        for (Point p : currentPosition.neighbors4()) {
            if (isValidPlayerPosition(p.x(),p.y())) {
                //Gdx.app.debug(TAG,"Point: " + p.x() +", " + p.y() + " score: " + pathMap[p.x()][p.y()] + " best: " + currentScore);
                if (pathMap[p.x()][p.y()] < currentScore && pathMap[p.x()][p.y()] >= 0) {

                    if (isCollision(Coord.get(p.x(),p.y()))) {
                        //if (currentScore == 2)
                        //    currentScore = 4; // another hack to get AI to move out of adjacent corners
                        continue;
                    }
                    nextPosition1 = Coord.get(p.x(), p.y());
                    break;
                }
            }
        }

        if (nextPosition1 != null)
            points.add(nextPosition1);

        return points;

    }


    public void printMap(int[][] pathMap) {
        String message;
        int tile;
        for (int y = height-1; y >= 0; y--) {
            message = "";
            for (int x = 0; x < width-1;x++){
                tile = pathMap[x] [y];
                if (tile < 0)
                    message = message.concat("|   ");
                else if (tile < 10)
                    message = message.concat("|  "+tile);
                else if (tile < 100)
                    message = message.concat("| "+tile);
                else
                    message = message.concat("|"+tile);
            }
            Gdx.app.log("Map: ", message);
        }

    }
}
