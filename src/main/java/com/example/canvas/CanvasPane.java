package com.example.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * Класс для работы с холстом приложения.
 * <p>
 * Этот класс предоставляет функциональность для создания и управления холстом,
 * включая его очистку и отрисовку начальной сетки.
 * </p>
 */
public class CanvasPane {

    /**
     * Объект холста для рисования фигур и других графических элементов.
     */
    private final Canvas canvas;
    private double scale = 1.0; // Масштаб по умолчанию
    private double offsetX = 0; // Смещение по оси X
    private double offsetY = 0; // Смещение по оси Y

    /**
     * Конструктор класса CanvasPane.
     * <p>
     * Создает новый холст с заданными размерами, очищает его
     * и рисует на нем начальную сетку.
     * </p>
     */
    public CanvasPane() {
        this.canvas = new Canvas(900, 1000);
        clearCanvas();
        drawInitialGrid();
    }

    /**
     * Возвращает объект холста.
     *
     * @return объект {@link Canvas}, представляющий холст для рисования
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Возвращает графический контекст холста.
     * <p>
     * Графический контекст используется для выполнения операций рисования на холсте.
     * </p>
     *
     * @return объект {@link GraphicsContext}, представляющий контекст рисования
     */
    public GraphicsContext getGraphicsContext() {
        return canvas.getGraphicsContext2D();
    }

    /**
     * Очищает холст, заполняя его белым цветом.
     */
    public void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Рисует начальную сетку на холсте.
     * <p>
     * Сетка состоит из вертикальных и горизонтальных линий с шагом 20 пикселей.
     * </p>
     */
    public void drawInitialGrid() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);

        double step = 20 * scale; // Масштабируем шаг сетки в зависимости от масштаба

        // Рисуем вертикальные линии с учетом масштаба
        for (double x = 0; x < canvas.getWidth(); x += step) {
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }

        // Рисуем горизонтальные линии с учетом масштаба
        for (double y = 0; y < canvas.getHeight(); y += step) {
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }
    }

    // Метод для установки масштаба
    public void setScale(double scale) {
        this.scale = scale;
        clearCanvas();         // Очищаем холст перед перерисовкой
        drawInitialGrid();     // Перерисовываем сетку с новым масштабом
        updateOffset(); // Пересчитываем смещение, чтобы область была по центру
        drawShapes();          // Перерисовываем все фигуры с новым масштабом
    }

    // Обновление смещения
    private void updateOffset() {
        offsetX = (canvas.getWidth() - (canvas.getWidth() * scale)) / 2;
        offsetY = (canvas.getHeight() - (canvas.getHeight() * scale)) / 2;
    }

        private void drawShapes() {
    }



    public double getScale() {
        return scale;
    }


}
