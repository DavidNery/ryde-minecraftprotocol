package com.github.davidnery.rydeminecraftprotocol.gui;

import com.github.davidnery.rydeminecraftprotocol.gui.scenes.MainScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ryde MinecraftProtocol");
        primaryStage.setResizable(false);
        primaryStage.setScene(new MainScene(primaryStage));
        primaryStage.show();
    }
}
