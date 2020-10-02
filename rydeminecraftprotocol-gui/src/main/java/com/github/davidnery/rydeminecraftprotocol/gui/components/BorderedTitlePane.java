package com.github.davidnery.rydeminecraftprotocol.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.StackPane;

public class BorderedTitlePane extends StackPane {

    public BorderedTitlePane(String titleString, Node content) {
        Label title = new Label("  " + titleString + "  ");
        title.setStyle("-fx-background-color: white; -fx-text-fill: gray;" +
                "-fx-translate-x: 10; -fx-translate-y: -10;");

        StackPane.setAlignment(title, Pos.TOP_LEFT);

        StackPane contentPane = new StackPane();
        contentPane.setPadding(new Insets(8));
        contentPane.getChildren().add(content);

        setStyle("-fx-content-display: top; -fx-border-color: darkgray; -fx-border-radius: 2");
        setBlendMode(BlendMode.MULTIPLY);
        getChildren().addAll(title, contentPane);
    }

}