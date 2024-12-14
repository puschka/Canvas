package com.example.canvas;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Главный класс приложения для генерации случайных фигур.
 * <p>
 * Этот класс отвечает за инициализацию JavaFX-приложения, создание графического интерфейса и запуск программы.
 * </p>
 */
public class MainApp extends Application {

    /**
     * Логгер для записи информации о работе приложения.
     */
    private static final Logger logger = LogManager.getLogger(MainApp.class);

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        logger.info("Запуск приложения");
        launch(args);
    }

    /**
     * Метод, вызываемый при старте JavaFX-приложения.
     *
     * @param primaryStage основной контейнер для отображения GUI
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Инициализация главного окна");
            // Устанавливаем заголовок окна
            primaryStage.setTitle("Генератор случайных фигур");

            // Создаем основное содержимое интерфейса
            VBox root = new VBox();
            CanvasPane canvasPane = new CanvasPane();
            ControlPanel controlPanel = new ControlPanel(canvasPane);

            // Добавляем элементы управления и холст в корневой элемент
            root.getChildren().addAll(controlPanel.getPane(), canvasPane.getCanvas());

            // Создаем сцену с заданными размерами
            Scene scene = new Scene(root, 900, 1000);
            primaryStage.setScene(scene);

            // Отображаем окно
            primaryStage.show();
            logger.info("Приложение успешно запущено");
        } catch (Exception e) {
            logger.error("Ошибка при запуске приложения: ", e);
        }
    }
}
