package com.example.canvas;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для создания и настройки панели управления параметрами генерации фигур.
 * <p>
 * Панель управления предоставляет интерфейс для задания параметров генерации фигур,
 * таких как количество фигур, координаты, масштаб, кучность и другие параметры.
 * </p>
 */
public class ControlPanel {

    /**
     * Логгер для записи информации о работе панели управления.
     */
    private static final Logger logger = LogManager.getLogger(ControlPanel.class);

    /**
     * Панель управления в виде {@link GridPane}.
     */
    private final GridPane pane;

    /**
     * Конструктор класса ControlPanel.
     * <p>
     * Создает новую панель управления и настраивает ее элементы.
     * </p>
     *
     * @param canvasPane объект {@link CanvasPane}, с которым взаимодействует панель управления
     */
    public ControlPanel(CanvasPane canvasPane) {
        this.pane = new GridPane();
        configurePanel(canvasPane);
    }

    /**
     * Настраивает панель управления, добавляя текстовые поля, слайдеры, чекбоксы и кнопки.
     *
     * @param canvasPane объект {@link CanvasPane}, на котором будут рисоваться фигуры
     */
    private void configurePanel(CanvasPane canvasPane) {
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(5);

        // Создаем слайдер масштаба
        Slider scaleSlider = createSlider(1, 1.5, 1, "Масштаб:");
        Slider densitySlider = createSlider(15, 50, 1, "Кучность:");

        // Добавляем слушатель на изменение значения слайдера
        scaleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double scale = newValue.doubleValue();  // Получаем значение слайдера
            canvasPane.setScale(scale);  // Обновляем масштаб в CanvasPane
        });

        // Добавление слайдера на панель

        // Поля ввода количества фигур
        TextField tfLines = createTextField("Количество линий:");
        TextField tfCircles = createTextField("Количество кругов:");
        TextField tfRectangles = createTextField("Количество прямоугольников:");
        TextField tfTriangles = createTextField("Количество треугольников:");
        TextField tfParabolas = createTextField("Количество парабол:");
        TextField tfTrapezoids = createTextField("Количество трапеций:");

        // Поля ввода координат
        TextField tfMinX = createTextField("Минимальная X:");
        TextField tfMaxX = createTextField("Максимальная X:");
        TextField tfMinY = createTextField("Минимальная Y:");
        TextField tfMaxY = createTextField("Максимальная Y:");

        // Чекбоксы
        CheckBox fillShapes = new CheckBox("Заливка");
        CheckBox showGrid = new CheckBox("Показать сетку");
        showGrid.setSelected(true);

        // Кнопка генерации
        javafx.scene.control.Button generateButton = new javafx.scene.control.Button("Сгенерировать");
        generateButton.setOnAction(e -> {
            try {
                // Считывание значений из полей
                int lines = parseRequiredTextField(tfLines);
                int circles = parseRequiredTextField(tfCircles);
                int rectangles = parseRequiredTextField(tfRectangles);
                int triangles = parseRequiredTextField(tfTriangles);
                int parabolas = parseRequiredTextField(tfParabolas);
                int trapezoids = parseRequiredTextField(tfTrapezoids);

                int minX = parseRequiredTextField(tfMinX);
                int maxX = parseRequiredTextField(tfMaxX);
                int minY = parseRequiredTextField(tfMinY);
                int maxY = parseRequiredTextField(tfMaxY);

                boolean fill = fillShapes.isSelected();
                boolean grid = showGrid.isSelected();

                // Очистка холста
                canvasPane.clearCanvas();
                if (grid) {
                    canvasPane.drawInitialGrid();
                }

                // Подсветка области
                // При вызове highlightArea передаем значение масштаба
                ShapeGenerator generator = new ShapeGenerator(canvasPane.getGraphicsContext());
                generator.highlightArea(minX, maxX, minY, maxY, canvasPane.getScale());


                // Генерация фигур
                generator.generateShapes(lines, circles, rectangles, triangles, parabolas, trapezoids, fill,
                        scaleSlider.getValue(), densitySlider.getValue(), minX, maxX, minY, maxY);

                logger.info("Фигуры успешно сгенерированы.");
            } catch (NumberFormatException ex) {
                logger.error("Ошибка ввода: ", ex);
                showError("Ошибка", "Пожалуйста, заполните все поля корректными числовыми значениями.");
            }


        });

        // Расположение элементов в интерфейсе
// Расположение элементов в интерфейсе
        pane.add(new javafx.scene.control.Label("Количество линий:"), 0, 0);
        pane.add(tfLines, 1, 0); // Текстовое поле для количества линий
        pane.add(new javafx.scene.control.Label("Количество кругов:"), 0, 1);
        pane.add(tfCircles, 1, 1); // Текстовое поле для количества кругов
        pane.add(new javafx.scene.control.Label("Количество прямоугольников:"), 0, 2);
        pane.add(tfRectangles, 1, 2); // Текстовое поле для количества прямоугольников
        pane.add(new javafx.scene.control.Label("Количество треугольников:"), 0, 3);
        pane.add(tfTriangles, 1, 3); // Текстовое поле для количества треугольников
        pane.add(new javafx.scene.control.Label("Количество парабол:"), 0, 4);
        pane.add(tfParabolas, 1, 4); // Текстовое поле для количества парабол
        pane.add(new javafx.scene.control.Label("Количество трапеций:"), 0, 5);
        pane.add(tfTrapezoids, 1, 5); // Текстовое поле для количества трапеций

// Расположение координат в другом ряду (справа)
        pane.add(new javafx.scene.control.Label("Минимальная X:"), 2, 0);
        pane.add(tfMinX, 3, 0); // Текстовое поле для минимальной X
        pane.add(new javafx.scene.control.Label("Максимальная X:"), 2, 1);
        pane.add(tfMaxX, 3, 1); // Текстовое поле для максимальной X
        pane.add(new javafx.scene.control.Label("Минимальная Y:"), 2, 2);
        pane.add(tfMinY, 3, 2); // Текстовое поле для минимальной Y
        pane.add(new javafx.scene.control.Label("Максимальная Y:"), 2, 3);
        pane.add(tfMaxY, 3, 3); // Текстовое поле для максимальной Y

        pane.add(new javafx.scene.control.Label("Настройки:"), 4, 0);
        pane.add(new javafx.scene.control.Label("Масштаб:"), 4, 1);
        pane.add(scaleSlider, 4, 2);
        pane.add(new javafx.scene.control.Label("Кучность:"), 4, 3);
        pane.add(densitySlider, 4, 4);
        pane.add(fillShapes, 5, 0);
        pane.add(showGrid, 5, 1);
        pane.add(generateButton, 5, 2);

    }

    /**
     * Создает текстовое поле для ввода числовых данных.
     *
     * @param label метка для отображения в качестве подсказки
     * @return объект {@link TextField} с заданной меткой
     */
    private TextField createTextField(String label) {
        TextField textField = new TextField();
        textField.setPromptText(label);
        return textField;
    }

    /**
     * Создает слайдер для выбора числового значения.
     *
     * @param min      минимальное значение слайдера
     * @param max      максимальное значение слайдера
     * @param value    начальное значение слайдера
     * @param labelText текст метки, поясняющий назначение слайдера
     * @return объект {@link Slider} с заданными параметрами
     */
    private Slider createSlider(double min, double max, double value, String labelText) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit((max - min) / 4);
        slider.setBlockIncrement(0.1);
        return slider;
    }

    /**
     * Преобразует значение из текстового поля в число.
     *
     * @param textField текстовое поле для ввода данных
     * @return числовое значение, введенное в текстовое поле
     * @throws NumberFormatException если поле пустое или содержит некорректное значение
     */
    private int parseRequiredTextField(TextField textField) {
        String text = textField.getText();
        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("Пустое значение: " + textField.getPromptText());
        }
        return Integer.parseInt(text.trim());
    }

    /**
     * Показывает сообщение об ошибке пользователю.
     *
     * @param title   заголовок окна ошибки
     * @param message текст сообщения об ошибке
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Возвращает панель управления.
     *
     * @return объект {@link GridPane}, представляющий панель управления
     */
    public GridPane getPane() {
        return pane;
    }
}
