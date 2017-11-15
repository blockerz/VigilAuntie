package com.lofisoftware.vigilauntie;

import com.badlogic.gdx.utils.Queue;
import com.lofisoftware.vigilauntie.entity.Entity;
import com.lofisoftware.vigilauntie.map.Map;

import static com.lofisoftware.vigilauntie.Constants.MAX_MESSAGES;

public class MessageManager {

    private static MessageManager instance;

    private boolean messageChanged = false;

    private Queue<String> messages;


    private MessageManager() {
        messages = new Queue<String>();
        messages.addLast("");
        messages.addLast("");
        messages.addLast("");

    }

    public static MessageManager getMessageManager() {

        if(instance == null) {
            instance = new MessageManager();
        }

        return instance;
    }

    public void sendMessage(String message) {

        if (message != null && !message.isEmpty()) {
            messageChanged = true;
            messages.addLast(message);
        }

        if (messages.size > MAX_MESSAGES) {
            messages.removeFirst();
        }
    }

    public Queue<String> getMessages() {
        return messages;
    }

    public boolean isMessageChanged() {
        return messageChanged;
    }

    public void setMessageChanged(boolean messageChanged) {
        this.messageChanged = messageChanged;
    }
}
