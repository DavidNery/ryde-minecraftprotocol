package com.github.davidnery.rydeminecraftprotocol.gui.scenes;

import com.github.davidnery.rydeminecraftprotocol.enums.ChatColor;
import com.github.davidnery.rydeminecraftprotocol.enums.MinecraftProtocolVersions;
import com.github.davidnery.rydeminecraftprotocol.gui.components.BorderedTitlePane;
import com.github.davidnery.rydeminecraftprotocol.models.Client;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound.PacketLoginInDisconnect;
import com.github.davidnery.rydeminecraftprotocol.packet.impl.clientbound.PacketLoginInLogin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScene extends Scene {

    private Client client;

    private Stage chatStage;

    public MainScene(Stage stage) {
        super(new GridPane());

        setFill(Color.LIGHTGRAY);

        GridPane pane = (GridPane) getRoot();

        GridPane content = new GridPane();
        content.setPadding(new Insets(20));
        content.setHgap(10);
        content.setVgap(10);

        GridPane serverPane = new GridPane();
        serverPane.setVgap(10);
        serverPane.setHgap(10);

        Text text = new Text("Server ip and version:");
        TextField serverIp = new TextField();
        serverIp.setText("localhost");

        ComboBox<String> comboBox = new ComboBox<>();
        for (MinecraftProtocolVersions versions : MinecraftProtocolVersions.values())
            comboBox.getItems().add(versions.getVersion());
        comboBox.setValue(comboBox.getItems().get(0));
        serverPane.addRow(0, text, serverIp, comboBox);

        Text valid = new Text("Valid");
        valid.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        valid.setFill(Color.web(ChatColor.GREEN.getColor()));
        text = new Text("Connect username:");
        TextField connectUsername = new TextField();
        connectUsername.setText("Notch");
        connectUsername.setOnKeyReleased(v -> {
            if (!connectUsername.getText().matches("^[A-Za-z0-9_]{3,16}$")) {
                valid.setFill(Color.web(ChatColor.DARK_RED.getColor()));
                valid.setText("Invalid");
            } else {
                valid.setFill(Color.web(ChatColor.GREEN.getColor()));
                valid.setText("Valid");
            }
        });
        serverPane.addRow(1, text, connectUsername, valid);

        content.add(new BorderedTitlePane("Connect Info", serverPane), 0, 0);

        ListView<String> console = new ListView<>();

        GridPane actionsPane = new GridPane();
        actionsPane.setHgap(10);
        actionsPane.setVgap(10);
        Button pingButton = new Button("Send ping");
        Button clearConsole = new Button("Clear Console");
        clearConsole.setOnMouseClicked(value -> console.getItems().clear());

        pingButton.setOnMouseClicked(value -> {
            pingButton.setDisable(true);
            String[] ip = serverIp.getText().split(":");

            console.getItems().add("Sending ping to " + ip[0] + "...");
            new Thread(() -> {
                try {
                    Client client = new Client(ip[0], ip.length == 1 ? 25565 : Integer.parseInt(ip[1]));
                    int serverProtocol = MinecraftProtocolVersions.byVersion(comboBox.getValue());
                    JsonObject pingResponse = new Gson().fromJson(client.ping(serverProtocol), JsonObject.class);

                    int realServerProtocol = pingResponse.getAsJsonObject("version").get("protocol").getAsInt();

                    JsonObject players = pingResponse.getAsJsonObject("players");
                    Platform.runLater(() ->
                            console.getItems().add("Server is running on protocol " + realServerProtocol +
                                    " with " + players.get("online") + "/" + players.get("max")
                                    + " players!")
                    );
                } catch (IOException e) {
                    Platform.runLater(() ->
                            console.getItems().add("Error \"" + e.getMessage() + "\" when trying to ping server!")
                    );
                }

                pingButton.setDisable(false);
            }).start();
        });

        Button connectButton = new Button("Connect");
        connectButton.setMaxWidth(Double.MAX_VALUE);
        Button disconnectButton = new Button("Disconnect");
        disconnectButton.setMaxWidth(Double.MAX_VALUE);
        disconnectButton.setDisable(true);

        connectButton.setOnMouseClicked(value -> {
            // https://help.minecraft.net/hc/en-us/articles/360034636712-Minecraft-Usernames
            if (!connectUsername.getText().matches("^[A-Za-z0-9_]{3,16}$")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("The nickname that you have inserted is invalid!");
                alert.show();
                return;
            }

            connectButton.setDisable(true);
            String[] ip = serverIp.getText().split(":");

            client = new Client(ip[0], ip.length == 1 ? 25565 : Integer.parseInt(ip[1]));

            int serverProtocol = MinecraftProtocolVersions.byVersion(comboBox.getValue());
            console.getItems().add("Connecting to " + client.getIp() + ":" + client.getPort()
                    + " using protocol " + serverProtocol + "...");

            client.subscribe(PacketLoginInLogin.class, packet -> {
                Platform.runLater(() -> {
                    console.getItems().add("Connected with name " + packet.getName() + " and uuid " + packet.getUuid().toString());

                    chatStage = new Stage();
                    chatStage.setScene(new ChatScene(client));
                    chatStage.setResizable(false);
                    chatStage.setTitle("Chat");
                    chatStage.show();
                    stage.setOnCloseRequest(closeRequest -> {
                        try {
                            client.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            });

            client.subscribe(PacketLoginInDisconnect.class, packet -> {
                Platform.runLater(() ->
                        console.getItems().add("Disconnected because " + packet.getMessage())
                );
                try {
                    client.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (chatStage != null) chatStage.close();
                connectButton.setDisable(false);
                disconnectButton.setDisable(true);
            });

            new Thread(() -> {
                try {
                    client.login(connectUsername.getText(), serverProtocol);
                    disconnectButton.setDisable(false);
                } catch (IOException e) {
                    Platform.runLater(() ->
                            console.getItems().add("Error \"" + e.getMessage() + "\" when trying to connect to server!")
                    );
                    connectButton.setDisable(false);
                }
            }).start();
        });
        disconnectButton.setOnMouseClicked(value -> {
            try {
                console.getItems().add("Disconnected");
                disconnectButton.setDisable(true);
                connectButton.setDisable(false);
                if (chatStage != null) chatStage.close();
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        actionsPane.addRow(0, pingButton, clearConsole);
        actionsPane.addRow(1, connectButton, disconnectButton);
        content.add(new BorderedTitlePane("Actions", actionsPane), 1, 0);
        pane.addColumn(0, content, console);
    }

}
