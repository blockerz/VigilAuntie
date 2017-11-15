package com.lofisoftware.vigilauntie;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.lofisoftware.vigilauntie.command.Command;
import com.lofisoftware.vigilauntie.entity.Entity;
import com.lofisoftware.vigilauntie.map.MapManager;

import java.util.Comparator;
import java.util.Iterator;
import static com.lofisoftware.vigilauntie.Constants.TURN_ANIMATION_TIME;

public class TurnManager {

    private static final String TAG = TurnManager.class.getSimpleName();

    private Array<Entity> awaitingTurn;

    private Array<Command> commands;

    private boolean roundComplete = true;
    private boolean commandRunning = false;
    private float commandElapsedTime;

    public boolean isRoundComplete() {
        return roundComplete;
    }

    public TurnManager() {
        awaitingTurn = new Array<>();
        commands = new Array<>();
        commandElapsedTime = 0;
    }

    public void update(float delta) {

        if (roundComplete) {
            awaitingTurn.clear();
            awaitingTurn.addAll(MapManager.getMapManager().getCurrentMap().getMapEntities());
            commands.clear();
            roundComplete = false;
        }

        if (awaitingTurn.size > 0) {
            getCommands();

            if (awaitingTurn.size == 0)
                commands.sort(new TurnOrderComparator());
        }
        else {
            //Gdx.app.debug(TAG, "Executing Commands!");
            if (executeCommands(delta)) {
                roundComplete = true;
            }
        }

    }

    private void getCommands() {
        for(Iterator<Entity> iter = awaitingTurn.iterator(); iter.hasNext();) {
            Entity entity = iter.next();

            entity.resetResiliance();

//            if (entity.isStunned()) {
//                Gdx.app.debug(TAG, entity.getName() + " Enter command was skipped due to stun.");
//                entity.resetResiliance();
//                iter.remove(); // Lose turn if stunned
//            }
//            else {
                Command command = entity.getNextCommand();

                if (command != null) {
                    Gdx.app.debug(TAG, entity.getName() + " Received command: " + command.toString());
                    commands.add(command);
                    iter.remove();
                }
//            }
        }
    }

    private boolean executeCommands(float delta) {

        if (commandRunning) {
            commandElapsedTime += delta;

            if (commandElapsedTime > TURN_ANIMATION_TIME) {
                commandElapsedTime = 0;
                commandRunning = false;
            }
        }

        if (!commandRunning) {
            //commands.sort(new TurnOrderComparator());

            if (commands.size > 0) {
                Command command = commands.pop();

                if (command != null) {
                    Entity entity = command.getEntity();

                    if (entity != null) {
                        if (entity.isStunned()) {
                            Gdx.app.debug(TAG, entity.getName() + " Execute command was skipped due to stun.");
                            entity.resetResiliance(); // Lose turn and reset resiliance
                        } else {

                            Gdx.app.debug(TAG, entity.getName() + " Commands Execute: " + command.toString());
                            command.execute();
                            commandRunning = true;
                        }
                    }
                }
            }
            else {
                return true; // no more commands
            }
        }

        return false;
    }

    public class TurnOrderComparator implements Comparator<Command> {
        @Override
        public int compare(Command arg0, Command arg1) {
            if (arg0.getSpeed() < arg1.getSpeed()) {
                return -1;
            } else if (arg0.getSpeed() == arg1.getSpeed()) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
