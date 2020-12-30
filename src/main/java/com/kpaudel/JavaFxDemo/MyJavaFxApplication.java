package com.kpaudel.JavaFxDemo;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class MyJavaFxApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        this.applicationContext = new SpringApplicationBuilder(MainApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationEvent event = new StageReadyEvent(primaryStage);
        this.applicationContext.publishEvent(event);
    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();
        Platform.exit();
    }

    static class StageReadyEvent extends ApplicationEvent {
        /**
         * Create a new {@code ApplicationEvent}.
         *
         * @param source the object on which the event initially occurred or with
         *               which the event is associated (never {@code null})
         */
        public StageReadyEvent(Object source) {
            super(source);
        }

        public Stage getStage() {
            return ((Stage) this.getSource());
        }
    }
}
