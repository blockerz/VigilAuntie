package com.lofisoftware.vigilauntie.map;

import com.badlogic.gdx.Gdx;
import com.lofisoftware.vigilauntie.TurnManager;
import com.lofisoftware.vigilauntie.entity.Entity;
import com.lofisoftware.vigilauntie.profile.ProfileManager;
import com.lofisoftware.vigilauntie.profile.ProfileObserver;


public class MapManager implements ProfileObserver {

    private static final String TAG = MapManager.class.getSimpleName();
    private static MapManager instance;

    private boolean mapChanged = true;
    private Map currentMap;
    private Entity player1;

    private TurnManager turnManager;

    private MapManager() {
        turnManager = new TurnManager();

    }

    public static MapManager getMapManager() {

        if(instance == null)
            instance = new MapManager();

        return instance;
    }

    public void loadMap(MapFactory.MapType type) {
        Map map = MapFactory.getMap(type);

        if( map == null ){
            Gdx.app.debug(TAG, "Map does not exist!  ");
            return;
        }

        currentMap = map;
        //mapChanged = true;
    }

    public Map getCurrentMap(){
        if( currentMap == null ) {
            loadMap(MapFactory.MapType.STREET);
            currentMap.calculatePath(player1.getCurrentPosition().getX(), player1.getCurrentPosition().getY());
        }
        return currentMap;
    }

    public void update(float delta) {
        if (currentMap != null) {
            currentMap.update(delta);
            turnManager.update(delta);

            if (turnManager.isRoundComplete()) {
                currentMap.calculatePath(player1.getCurrentPosition().getX(), player1.getCurrentPosition().getY());
            }
        }


//        if (player1 != null) {
//            player1.update(delta);
//        }
    }

    public void updateMap() {
        mapChanged = true;
    }

    public void mapUpdated() {
        mapChanged = false;
    }

    public boolean isChanged() {
        return mapChanged;
    }

    @Override
    public void onNotify(ProfileManager profileManager, ProfileObserver.ProfileEvent event) {
        switch(event){
            case PROFILE_LOADED:
                String currentMapType = profileManager.getProperty("currentMapType", String.class);
                MapFactory.MapType mapType;
                if( currentMapType == null || currentMapType.isEmpty() ){
                    mapType = MapFactory.MapType.DOWNTOWN;
                }else{
                    mapType = MapFactory.MapType.valueOf(currentMapType);
                }
                loadMap(mapType);

                /*
                Vector2 topWorldMapStartPosition = profileManager.getProperty("topWorldMapStartPosition", Vector2.class);
                if( topWorldMapStartPosition != null ){
                    MapFactory.getMap(MapFactory.MapType.TOP_WORLD).setPlayerStart(topWorldMapStartPosition);
                }

                Vector2 castleOfDoomMapStartPosition = profileManager.getProperty("castleOfDoomMapStartPosition", Vector2.class);
                if( castleOfDoomMapStartPosition != null ){
                    MapFactory.getMap(MapFactory.MapType.CASTLE_OF_DOOM).setPlayerStart(castleOfDoomMapStartPosition);
                }

                Vector2 townMapStartPosition = profileManager.getProperty("townMapStartPosition", Vector2.class);
                if( townMapStartPosition != null ){
                    MapFactory.getMap(MapFactory.MapType.TOWN).setPlayerStart(townMapStartPosition);
                }
                */

                break;
            case SAVING_PROFILE:
                if(currentMap != null ){
                    profileManager.setProperty("currentMapType", MapFactory.MapType.DOWNTOWN.toString()/*currentMap.currentMapType.toString()*/);
                }

                //profileManager.setProperty("topWorldMapStartPosition", MapFactory.getMap(MapFactory.MapType.TOP_WORLD).getPlayerStart() );
                //profileManager.setProperty("castleOfDoomMapStartPosition", MapFactory.getMap(MapFactory.MapType.CASTLE_OF_DOOM).getPlayerStart() );
                //profileManager.setProperty("townMapStartPosition", MapFactory.getMap(MapFactory.MapType.TOWN).getPlayerStart() );
                break;
            case CLEAR_CURRENT_PROFILE:
                currentMap = null;
                profileManager.setProperty("currentMapType", MapFactory.MapType.DOWNTOWN.toString());

                MapFactory.clearCache();

                //profileManager.setProperty("topWorldMapStartPosition", MapFactory.getMap(MapFactory.MapType.TOP_WORLD).getPlayerStart() );
                //profileManager.setProperty("castleOfDoomMapStartPosition", MapFactory.getMap(MapFactory.MapType.CASTLE_OF_DOOM).getPlayerStart() );
                //profileManager.setProperty("townMapStartPosition", MapFactory.getMap(MapFactory.MapType.TOWN).getPlayerStart() );
                break;
            default:
                break;
        }
    }

    public Entity getPlayer() {
        return player1;
    }

    public void setPlayer(Entity player) {
        this.player1 = player;
    }
}
