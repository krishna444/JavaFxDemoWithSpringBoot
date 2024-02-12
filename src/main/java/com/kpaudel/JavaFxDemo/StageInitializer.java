package com.kpaudel.JavaFxDemo;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<MyJavaFxApplication.StageReadyEvent> {
    @Autowired
    private Environment environment;

    private String player1, player2, none;
    private String player1Style, player2Style, noneStyle;
    private String currentPlayer;
    private Cell[][] cell = new Cell[3][3];
    private Button resetButton = new Button("Reset");
    private Text statusLabel = new Text();
    @Value("classpath:/test.fxml")
    private Resource testResource;
    private final String applicationTitle;
    private final ApplicationContext applicationContext;

    @Autowired
    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle, Environment environment, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
        this.environment = environment;
        this.player1 = this.environment.getProperty("player1");
        this.currentPlayer = player1;
        this.player2 = this.environment.getProperty("player2");
        this.none = " ";
        this.player1Style = this.environment.getProperty("player1.style");
        this.player2Style = this.environment.getProperty("player2.style");
        this.noneStyle = this.environment.getProperty("default.style");
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
        pane.getChildren().add(this.resetButton);
        this.resetButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        this.resetButton.setTranslateY(610);
        this.resetButton.setOnAction(e -> {
            reset();
        });
        pane.getChildren().add(statusLabel);
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        statusLabel.setTranslateY(700);
        statusLabel.setText("Player " + currentPlayer + "'s turn");


        Stage stage = event.getStage();
        Scene scene = new Scene(pane, 600, 750);
        stage.setTitle(this.applicationTitle);
        stage.setScene(scene);
        stage.show();

    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cell[i][j].getPlayer() == none) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasWon(String player) {
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

    public void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cell[i][j].setText(none);
                cell[i][j].setStyle(noneStyle);
                cell[i][j].player = none;
            }
        }
        this.currentPlayer = player1;
        this.statusLabel.setText("Player " + currentPlayer + "'s turn");
    }


    private class Cell extends Button {
        private String player = none;

        public Cell() {
            setPrefSize(200, 200);
            setStyle(noneStyle);
            setOnAction(e -> {
                if (player == none && currentPlayer != none) {
                    player = currentPlayer;
                    setText(player);
                    setStyle(currentPlayer == player1 ? player1Style : player2Style);
                    if (hasWon(currentPlayer)) {
                        statusLabel.setText("Player " + currentPlayer + " has won");
                        currentPlayer = none;
                    } else if (isBoardFull()) {
                        statusLabel.setText("It's a draw");
                        currentPlayer = none;
                    } else {
                        currentPlayer = (currentPlayer == player1) ? player2 : player1;
                        statusLabel.setText("Player " + currentPlayer + "'s turn");
                    }
                }
            });
        }

        public String getPlayer() {
            return player;
        }
    }
}
