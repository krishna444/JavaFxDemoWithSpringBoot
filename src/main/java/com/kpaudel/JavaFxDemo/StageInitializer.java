package com.kpaudel.JavaFxDemo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<MyJavaFxApplication.StageReadyEvent> {
    private char currentPlayer = 'X';
    private Cell[][] cell = new Cell[3][3];
    private Text statusLabel = new Text();
    @Value("classpath:/test.fxml")
    private Resource testResource;
    private final String applicationTitle;
    private final ApplicationContext applicationContext;

    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(MyJavaFxApplication.StageReadyEvent event) {
        GridPane gridPane = new GridPane();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cell[i][j] = new Cell();
                gridPane.add(cell[i][j], j, i);
            }
        }

        Pane pane = new Pane();
        pane.getChildren().add(gridPane);
        pane.getChildren().add(statusLabel);
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        statusLabel.setTranslateY(330);

        Stage stage = event.getStage();
        Scene scene = new Scene(pane, 300, 350);
        stage.setScene(scene);
        stage.show();

    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cell[i][j].getPlayer() == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasWon(char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (cell[i][0].getPlayer() == player && cell[i][1].getPlayer() == player && cell[i][2].getPlayer() == player) {
                return true;
            }
        }
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (cell[0][j].getPlayer() == player && cell[1][j].getPlayer() == player && cell[2][j].getPlayer() == player) {
                return true;
            }
        }
        // Check diagonals
        if (cell[0][0].getPlayer() == player && cell[1][1].getPlayer() == player && cell[2][2].getPlayer() == player) {
            return true;
        }
        if (cell[0][2].getPlayer() == player && cell[1][1].getPlayer() == player && cell[2][0].getPlayer() == player) {
            return true;
        }
        return false;
    }

    private class Cell extends Button {
        private char player = ' ';

        public Cell() {
            setPrefSize(100, 100);
            setStyle("-fx-font-size: 3em;");
            setOnAction(e -> {
                if (player == ' ' && currentPlayer != ' ') {
                    player = currentPlayer;
                    setText(String.valueOf(player));
                    if (hasWon(currentPlayer)) {
                        statusLabel.setText("Player " + currentPlayer + " has won");
                        currentPlayer = ' ';
                    } else if (isBoardFull()) {
                        statusLabel.setText("It's a draw");
                        currentPlayer = ' ';
                    } else {
                        currentPlayer = (currentPlayer == 'X') ? '0' : 'X';
                        statusLabel.setText("Player " + currentPlayer + "'s turn");
                    }
                }
            });


        }

        public char getPlayer() {
            return player;
        }
    }
}
