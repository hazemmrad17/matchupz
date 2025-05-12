package models.logistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.logistics.Message;

public class ChatManager {
    private static ChatManager instance;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private final ObservableList<String> modules = FXCollections.observableArrayList(
            "Fournisseurs", "Admin", "Espaces", "Match", "Sponsor", "Teams" // Liste des modules
    );

    private ChatManager() {}

    public static ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    public ObservableList<Message> getMessages() {
        return messages;
    }

    public ObservableList<Message> getMessagesForModule(String module) {
        return FXCollections.observableArrayList(
                messages.filtered(msg -> module.equals(msg.getRecipient()))
        );
    }

    public ObservableList<String> getModules() {
        return modules;
    }

    public void sendMessage(String sender, String recipient, String content) {
        messages.add(new Message(sender, recipient, content));
    }
}