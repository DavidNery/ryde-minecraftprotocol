package com.github.davidnery.rydeminecraftprotocol.gui.scenes;

import com.github.davidnery.rydeminecraftprotocol.models.Client;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound.PacketPlayInChatMessage;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.serverbound.PacketPlayOutChatMessage;
import com.github.davidnery.rydeminecraftprotocol.utils.ChatUtils;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.TextFlow;

import java.io.IOException;

public class ChatScene extends Scene {

    public ChatScene(Client client) {
        super(new GridPane(), 700, 430);

        GridPane pane = (GridPane) getRoot();

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(getClass().getClassLoader().getResourceAsStream("chat_background.png")),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT
        );
        pane.setBackground(new Background(backgroundImage));

        ListView<TextFlow> console = new ListView<>();
        console.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3)");
        console.setCellFactory(list -> new ListCell<TextFlow>() {
            @Override
            protected void updateItem(TextFlow item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty)
                    setGraphic(item);
                else
                    setGraphic(null);
                setStyle("-fx-background-color: rgba(0, 0, 0, 0.3)");
            }
        });
        console.setPrefSize(700, 400);

        TextField text = new TextField();
        text.setPrefSize(700, 30);
        text.textProperty().addListener((ov, oldValue, newValue) -> {
            // https://wiki.vg/Protocol#Chat_Message_2
            if (text.getText().length() > 100) // maximum length of a msg that minecraft server accepts
                text.setText(text.getText().substring(0, 100));
        });
        text.setOnKeyReleased(value -> {
            if (value.getCode() == KeyCode.ENTER) {
                if (text.getText().length() > 100) return;

                PacketPlayOutChatMessage packet = new PacketPlayOutChatMessage(text.getText());
                try {
                    client.sendPacket(packet);
                    text.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pane.addColumn(0, console, text);

        client.subscribe(PacketPlayInChatMessage.class, packet ->
                Platform.runLater(() ->
                        console.getItems().add(ChatUtils.parseMessage(packet.getMessage())
                        )
                )
        );
    }

}
