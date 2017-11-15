package com.lofisoftware.vigilauntie;


import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Queue;
import com.lofisoftware.vigilauntie.entity.CharacterStats;
import com.lofisoftware.vigilauntie.map.MapManager;

import squidpony.squidgrid.gui.gdx.SquidMessageBox;
import squidpony.squidgrid.gui.gdx.TextCellFactory;

import static com.lofisoftware.vigilauntie.Constants.CELL_HEIGHT;
import static com.lofisoftware.vigilauntie.Constants.CELL_WIDTH;
import static com.lofisoftware.vigilauntie.Constants.SCREEN_WIDTH_CELLS;
import static com.lofisoftware.vigilauntie.Constants.STATUS_HEIGHT_CELLS;
import static com.lofisoftware.vigilauntie.Constants.UI_SCALE_FACTOR;

public class StatusUI extends Window {

    private int hpValue;
    private int timeValue;
    private int levelValue;
    private int xpValue;
    private int scoreValue;

    private Label hpValueLabel;
    private Label timeValueLabel;
    private Label levelValueLabel;
    private Label xpValueLabel;
    private Label scoreValueLabel;
    private Label message1;
    private Label message2;
    private Label message3;

    //private SquidMessageBox messageBox;

    public StatusUI() {
        super("", Utility.STATUSUI_SKIN);

        hpValue = 999;
        timeValue = 999;
        levelValue = 99;
        xpValue = 999;
        scoreValue = 99999;

        //messageBox = new SquidMessageBox(50,8);
        //messageBox.getTextCellFactory().font(Utility.STATUSUI_SKIN.getFont("default-font")).initByFont();
        //messageBox.setBounds(0,0,30,5);
        //messageBox.setSize(SCREEN_WIDTH_CELLS*CELL_WIDTH * UI_SCALE_FACTOR/2,STATUS_HEIGHT_CELLS*CELL_HEIGHT * UI_SCALE_FACTOR/2);
        //messageBox.appendMessage("Test MEssage");
        //messageBox.appendMessage("");
        //messageBox.appendMessage("Test MEssage");
        //messageBox.appendMessage("");
        //messageBox.appendWrappingMessage("Test MEssageTest MEssageTest MEssageTest MEssageTest MEssage");

        this.pad(5,5,5,5);
        //WidgetGroup group1 = new WidgetGroup();
        //WidgetGroup group2 = new WidgetGroup();

        Label hpLabel = new Label("HP: ", Utility.STATUSUI_SKIN);
        Label timeLabel = new Label("       Time: ", Utility.STATUSUI_SKIN);
        Label levelLabel = new Label("Level: ", Utility.STATUSUI_SKIN);
        Label xpLabel = new Label("XP: ", Utility.STATUSUI_SKIN);
        Label scoreLabel = new Label("Score: ", Utility.STATUSUI_SKIN);

        hpValueLabel = new Label(String.valueOf(hpValue), Utility.STATUSUI_SKIN);
        timeValueLabel= new Label(String.valueOf(timeValue), Utility.STATUSUI_SKIN);
        levelValueLabel= new Label(String.valueOf(levelValue), Utility.STATUSUI_SKIN);
        xpValueLabel= new Label(String.valueOf(xpValue), Utility.STATUSUI_SKIN);
        scoreValueLabel= new Label(String.valueOf(scoreValue), Utility.STATUSUI_SKIN);

        message1 = new Label(" ", Utility.STATUSUI_SKIN);
        message2 = new Label(" ", Utility.STATUSUI_SKIN);
        message3 = new Label(" ", Utility.STATUSUI_SKIN);

        defaults().expand().fill().pad(0, 0, 0, 0);

//        this.add(hpLabel).align(Align.left);
//        this.add(hpValueLabel).align(Align.right);
//        this.add();
//        this.add(timeLabel).align(Align.left);
//        this.add(timeValueLabel).align(Align.right);
//        this.add();
//        this.add(xpLabel).align(Align.left);
//        this.add(xpValueLabel).align(Align.right);

        Table subTable = new Table();
        subTable.add(hpLabel).right().top().expandX();
        subTable.add(hpValueLabel).left().top().expandX();
        subTable.add(timeLabel).right().top().expandX();
        subTable.add(timeValueLabel).left().top().expandX();

        Table messageTable = new Table();
        messageTable.defaults().left().top().height(16).width(20);
        messageTable.add(message1);
        messageTable.row();
        messageTable.add(message2);
        messageTable.row();
        messageTable.add(message3);

        Table subTable3 = new Table();
        subTable3.add(subTable).left().top().expandX();
        subTable3.row();
        subTable3.add(messageTable).left().top().expandY();


        Table subTable2 = new Table();
        subTable2.add(xpLabel).right().top().expand();
        subTable2.add(xpValueLabel).left().top().expand();
        subTable2.row();
        subTable2.add(levelLabel).right().top().expand();
        subTable2.add(levelValueLabel).left().top().expand();
        subTable2.row();
        subTable2.add(scoreLabel).right().top().expand();
        subTable2.add(scoreValueLabel).left().top().expand();

        this.add(subTable3).expand();
        this.add(subTable2).expand();

        this.pack();


    }


    public void update () {
        CharacterStats stats = MapManager.getMapManager().getPlayer().getStats();

        if (stats != null) {
            hpValue = Math.max(0,stats.getHitPoints());
            xpValue = stats.getExperience();
            scoreValue = stats.getScore();
            levelValue = stats.getLevel();
            timeValue = stats.getMoves();

            hpValueLabel.setText(String.valueOf(hpValue));
            xpValueLabel.setText(String.valueOf(xpValue));
            scoreValueLabel.setText(String.valueOf(scoreValue));
            levelValueLabel.setText(String.valueOf(levelValue));
            timeValueLabel.setText(String.valueOf(timeValue));


        }

        if (MessageManager.getMessageManager().isMessageChanged()) {
            addMessages();
        }

    }

    private void addMessages() {

        Queue<String> messages = MessageManager.getMessageManager().getMessages();

        if (messages != null) {
            message1.setText(messages.get(messages.size-3));
            message2.setText(messages.get(messages.size-2));
            message3.setText(messages.get(messages.size-1));
            MessageManager.getMessageManager().setMessageChanged(false);
        }

    }
}
