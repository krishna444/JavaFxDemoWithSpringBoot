package com.kpaudel.JavaFxDemo;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import org.springframework.stereotype.Component;

@Component
public class TestController {
    @FXML
    public LineChart<String, Double> chart;
}
