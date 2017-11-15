package com.lofisoftware.vigilauntie.map;

import com.lofisoftware.vigilauntie.entity.EntityFactory;

import java.util.Hashtable;

import squidpony.squidmath.LightRNG;
import squidpony.squidmath.RNG;

public class MapFactory {

    private static final String TAG = MapFactory.class.getSimpleName();

    private static RNG rng = new RNG(new LightRNG());

    public static void setRNGSeed(long seed) {
        rng = new RNG(new LightRNG(seed));
    }

    public static void setRNG(RNG rng) {
        MapFactory.rng = rng;
    }

//    public static Map generateEmptyMap(int width, int height) {
//        Map map = new Map(width, height);
//        return map;
//    }

    //All maps for the game
    private static Hashtable<MapType,Map> mapTable = new Hashtable<MapType, Map>();

    public static enum MapType{
        STREET,
        DOWNTOWN,
        TOWN,
        DUNGEON,
        CASTLE_OF_DOOM
    }

    static public Map getMap(MapType mapType){
        Map map = null;
        switch(mapType){
            case STREET:
                map = new Map(60,12);
                map.generateMapTileLayer("sprites/tiles/StreetTiles.png", "sprites/tiles/Background-Fence-Building.png");
                map.setEntity(2, 2, EntityFactory.getInstance().getEntity(EntityFactory.EntityType.MOB));
                map.getEntity(2,2).getStats().setName("Larry");
                map.setEntity(4, 4, EntityFactory.getInstance().getEntity(EntityFactory.EntityType.MOB));
                map.getEntity(4,4).getStats().setName("Curly");
                map.setEntity(5, 5, EntityFactory.getInstance().getEntity(EntityFactory.EntityType.MOB));
                map.getEntity(5,5).getStats().setName("Sue");
                map.setEntity(0, 10, EntityFactory.getInstance().getEntity(EntityFactory.EntityType.MOB));
                map.getEntity(0,10).getStats().setName("Jane");
                map.setEntity(20, 5, EntityFactory.getInstance().getEntity(EntityFactory.EntityType.MOB));
                map.getEntity(20,5).getStats().setName("Moe");
                break;
            case DOWNTOWN:
                map = mapTable.get(MapType.DOWNTOWN);
                if( map == null ){
                    //map = generateRexMap();
                    //map = new Map(30);


                   // mapTable.put(MapType.DOWNTOWN, map);
                }
                break;
//            case DOWNTOWN:
//                map = mapTable.get(MapType.DOWNTOWN);
//                if( map == null ){
//                    //map = generateEmptyMap(240,240);
//                    map = tileEmptyMapFromRex("rex/RoadNW-Grass-60x60.xp",4,4);
//                    Map parking = generateMapFromRexMap(getRexTileMapFromFile("rex/Parking-Grey-51x51.xp"));
//                    map.copyMapToOffset(parking,9,9);
//                    map.copyMapToOffset(parking,69,69);
//                    Map bank = generateMapFromRexMap(getRexTileMapFromFile("rex/Bank-Overrun-51x51.xp"));
//                    map.copyMapToOffset(bank,9,69);
//                    map.copyMapToOffset(bank,69,9);
//                    mapTable.put(MapType.DOWNTOWN, map);
//                }
//                break;
//            case TOWN:
//                map = mapTable.get(MapType.TOWN);
//                if( map == null ){
//                    map = generateRexMap();
//                    mapTable.put(MapType.TOWN, map);
//                }
//                break;
//            case CASTLE_OF_DOOM:
//                map = mapTable.get(MapType.CASTLE_OF_DOOM);
//                if( map == null ){
//                    map = generateRexMap();
//                    mapTable.put(MapType.CASTLE_OF_DOOM, map);
//                }
//                break;
            default:
                break;
        }
        return map;
    }

    public static void clearCache(){
        for( Map map: mapTable.values()){
            map.dispose();
        }
        mapTable.clear();
    }
}