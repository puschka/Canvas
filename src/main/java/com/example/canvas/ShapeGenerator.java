package com.example.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Класс для генерации случайных фигур.
 * <p>
 * Поддерживает создание линий, кругов, прямоугольников, треугольников, парабол и трапеций.
 * Фигуры ограничиваются заданной областью и могут быть закрашены или не закрашены.
 * </p>
 */
public class ShapeGenerator {

    private final GraphicsContext gc;
    private final Random random;

    public ShapeGenerator(GraphicsContext gc) {
        this.gc = gc;
        this.random = new Random();
    }

    /**
     * Генерация заданного количества фигур различных типов.
     *
     * @param lines      количество линий
     * @param circles    количество кругов
     * @param rectangles количество прямоугольников
     * @param triangles  количество треугольников
     * @param parabolas  количество парабол
     * @param trapezoids количество трапеций
     * @param fill       закрашивать фигуры или только рисовать контур
     * @param scale      масштабирование размеров фигур
     * @param density    плотность генерации координат (больше значение — более плотное размещение)
     * @param minX       минимальная координата X
     * @param maxX       максимальная координата X
     * @param minY       минимальная координата Y
     * @param maxY       максимальная координата Y
     */
    public void generateShapes(int lines, int circles, int rectangles, int triangles, int parabolas, int trapezoids,
                               boolean fill, double scale, double density, int minX, int maxX, int minY, int maxY) {
        if (lines > 0) generateLines(lines, minX, maxX, minY, maxY, density, scale);
        if (circles > 0) generateCircles(circles, fill, scale, minX, maxX, minY, maxY, density);
        if (rectangles > 0) generateRectangles(rectangles, fill, scale, minX, maxX, minY, maxY, density);
        if (triangles > 0) generateTriangles(triangles, fill, scale, minX, maxX, minY, maxY, density);
        if (parabolas > 0) generateParabolas(parabolas, fill, scale, minX, maxX, minY, maxY, density);
        if (trapezoids > 0) generateTrapezoids(trapezoids, fill, scale, minX, maxX, minY, maxY, density);
    }

    /**
     * Генерация линий.
     *
     * @param count   количество линий
     * @param minX    минимальная координата X
     * @param maxX    максимальная координата X
     * @param minY    минимальная координата Y
     * @param maxY    максимальная координата Y
     * @param density плотность генерации координат
     */
    private void generateLines(int count, int minX, int maxX, int minY, int maxY, double density, double scale) {
        gc.setStroke(Color.BLACK);
        double maxLength = 100 * scale; // Максимальная длина линии (например, 100 пикселей)

        for (int i = 0; i < count; i++) {
            double x1 = randomCoordinateWithDensity(minX, maxX, density);
            double y1 = randomCoordinateWithDensity(minY, maxY, density);

            double x2, y2;
            do {
                x2 = randomCoordinateWithDensity(minX, maxX, density);
                y2 = randomCoordinateWithDensity(minY, maxY, density);
            } while (Math.hypot(x2 - x1, y2 - y1) > maxLength);

            gc.strokeLine(x1, y1, x2, y2);
        }
    }


    /**
     * Генерация кругов.
     *
     * @param count   количество кругов
     * @param fill    закрашивать круги или только рисовать контур
     * @param scale   масштабирование размера кругов
     * @param minX    минимальная координата X
     * @param maxX    максимальная координата X
     * @param minY    минимальная координата Y
     * @param maxY    максимальная координата Y
     * @param density плотность генерации координат
     */
    private void generateCircles(int count, boolean fill, double scale, int minX, int maxX, int minY, int maxY, double density) {
        for (int i = 0; i < count; i++) {
            // Генерация случайных координат центра круга с учётом плотности
            double x = randomCoordinateWithDensity(minX, maxX, density);
            double y = randomCoordinateWithDensity(minY, maxY, density);

            // Масштабируем размер круга
            double size = Math.min(random.nextDouble() * 50 * scale, Math.min(maxX - x, maxY - y));

            // Корректировка координат и размера, чтобы круг не выходил за пределы рамки
            if (x + size > maxX) {
                x = maxX - size;  // Сдвигаем круг влево, чтобы он не выходил за пределы
            }
            if (y + size > maxY) {
                y = maxY - size;  // Сдвигаем круг вверх, чтобы он не выходил за пределы
            }

            // Отрисовка круга
            if (fill) {
                gc.setFill(randomColor());
                gc.fillOval(x, y, size, size); // Закрашиваем круг
            } else {
                gc.setStroke(randomColor());
                gc.strokeOval(x, y, size, size); // Рисуем контур круга
            }
        }
    }



    /**
     * Генерация случайных прямоугольников на холсте.
     * <p>
     * Метод генерирует прямоугольники в заданных пределах координат (minX, maxX) и (minY, maxY).
     * Для каждой фигуры случайным образом выбираются:
     * <ul>
     *     <li>Положение левого верхнего угла (x, y) с учетом плотности (density), которая регулирует вероятность появления фигуры в разных частях холста;</li>
     *     <li>Размеры прямоугольника (ширина и высота), которые также зависят от заданного коэффициента масштаба (scale), но ограничены размерами холста.</li>
     * </ul>
     * Также выбирается, будет ли прямоугольник закрашен или только обведен:
     * <ul>
     *     <li>Если флаг fill равен true, прямоугольник будет закрашен случайным цветом;</li>
     *     <li>Если флаг fill равен false, прямоугольник будет нарисован только контуром, также случайного цвета.</li>
     * </ul>
     * </p>
     *
     * @param count   количество генерируемых прямоугольников
     * @param fill    флаг, указывающий, следует ли закрашивать прямоугольники (true — закрашивать, false — только контур)
     * @param scale   коэффициент масштаба, который используется для регулирования размеров прямоугольников
     * @param minX    минимальная возможная координата по оси X для генерации прямоугольников
     * @param maxX    максимальная возможная координата по оси X для генерации прямоугольников
     * @param minY    минимальная возможная координата по оси Y для генерации прямоугольников
     * @param maxY    максимальная возможная координата по оси Y для генерации прямоугольников
     * @param density плотность распределения фигур по области холста (определяет, как часто будут встречаться фигуры в разных участках)
     */
    private void generateRectangles(int count, boolean fill, double scale, int minX, int maxX, int minY, int maxY, double density) {
        for (int i = 0; i < count; i++) {
            // Генерация случайных координат для прямоугольника с учётом плотности
            double x = randomCoordinateWithDensity(minX, maxX, density);
            double y = randomCoordinateWithDensity(minY, maxY, density);

            // Масштабируем размеры прямоугольника
            double width = Math.min(random.nextDouble() * 80 * scale, maxX - x);  // Применяем масштаб к ширине
            double height = Math.min(random.nextDouble() * 50 * scale, maxY - y); // Применяем масштаб к высоте

            // Ограничение, чтобы прямоугольник не выходил за пределы по X
            if (x + width > maxX) {
                width = maxX - x;  // Сужаем ширину, чтобы не выйти за пределы
            }

            // Ограничение, чтобы прямоугольник не выходил за пределы по Y
            if (y + height > maxY) {
                height = maxY - y; // Сужаем высоту, чтобы не выйти за пределы
            }

            // Отрисовка прямоугольника с учётом масштабирования
            if (fill) {
                gc.setFill(randomColor());
                gc.fillRect(x * scale, y * scale, width * scale, height * scale);  // Закрашиваем прямоугольник
            } else {
                gc.setStroke(randomColor());
                gc.strokeRect(x * scale, y * scale, width * scale, height * scale);  // Обводим прямоугольник
            }
        }
    }



    /**
     * Генерация случайных треугольников на холсте.
     * <p>
     * Метод генерирует треугольники, используя случайные координаты для трех вершин в пределах заданной области
     * с учётом плотности (density), которая влияет на распределение фигур по холсту.
     * Вершины треугольника выбираются случайным образом, при этом расстояние между вершинами ограничивается
     * коэффициентом масштаба (scale), а также размерами холста.
     * В зависимости от флага fill, треугольник может быть закрашен или нарисован только контуром.
     * <ul>
     *     <li>Для каждой фигуры генерируются координаты трех вершин: (x1, y1), (x2, y2) и (x3, y3).</li>
     *     <li>Координаты вершин генерируются с учётом плотности, которая влияет на вероятности попадания точек
     *     в разные области холста, и масштаба, который определяет максимальное расстояние между точками.</li>
     *     <li>Треугольник может быть нарисован либо закрашенным случайным цветом, либо только контуром, также случайного цвета.</li>
     * </ul>
     * </p>
     *
     * @param count   количество треугольников для генерации
     * @param fill    флаг, определяющий, нужно ли заполнять треугольники цветом (true — заполнять, false — рисовать только контур)
     * @param scale   коэффициент масштаба, влияющий на размеры генерируемых фигур
     * @param minX    минимальная возможная координата по оси X
     * @param maxX    максимальная возможная координата по оси X
     * @param minY    минимальная возможная координата по оси Y
     * @param maxY    максимальная возможная координата по оси Y
     * @param density плотность распределения фигур по холсту (влияет на вероятности появления фигур в разных частях холста)
     */
    private void generateTriangles(int count, boolean fill, double scale, int minX, int maxX, int minY, int maxY, double density) {
        for (int i = 0; i < count; i++) {
            // Генерация случайных координат для вершин треугольника с учётом кучности
            double x1 = randomCoordinateWithDensity(minX, maxX, density);
            double y1 = randomCoordinateWithDensity(minY, maxY, density);

            // Учитываем масштабирование и ограничиваем размеры
            double x2 = Math.min(x1 + random.nextDouble() * 50 * scale, maxX);
            double y2 = Math.min(y1 + random.nextDouble() * 50 * scale, maxY);

            double x3 = Math.max(x1 - random.nextDouble() * 50 * scale, minX);
            double y3 = Math.min(y1 + random.nextDouble() * 50 * scale, maxY);

            // Ограничиваем координаты треугольника, чтобы они не выходили за пределы области
            x1 = Math.max(minX, Math.min(maxX, x1));
            y1 = Math.max(minY, Math.min(maxY, y1));

            x2 = Math.max(minX, Math.min(maxX, x2));
            y2 = Math.max(minY, Math.min(maxY, y2));

            x3 = Math.max(minX, Math.min(maxX, x3));
            y3 = Math.max(minY, Math.min(maxY, y3));

            // Если фигура должна быть закрашена
            if (fill) {
                gc.setFill(randomColor());
                gc.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
            } else {
                // Если фигура только с контуром
                gc.setStroke(randomColor());
                gc.strokePolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
            }
        }
    }


    /**
     * Генерация случайных парабол на холсте с возможностью их закрашивания.
     * <p>
     * Метод генерирует параболы в пределах заданных координат (minX, maxX) и (minY, maxY).
     * Параболы имеют случайное расположение (центр), а также случайные размеры, которые регулируются масштабом (scale).
     * Генерация осуществляется через вычисление множества точек, которые составляют кривую параболы.
     * Количество точек (steps) определяет точность рисования параболы.
     * В зависимости от флага fill, парабола может быть закрашена или нарисована только контуром.
     * <ul>
     *     <li>Парабола строится с использованием одного центрального положения (x, y), которое генерируется случайным образом.</li>
     *     <li>Максимальная ширина и высота параболы зависят от масштаба и ограничены размерами холста, чтобы избежать выхода за его пределы.</li>
     *     <li>Каждая парабола генерируется с помощью набора точек (xPoints, yPoints), которые равномерно распределены вдоль оси X в пределах выбранной ширины.</li>
     *     <li>Значение t (от 0 до steps) определяет положение каждой точки на параболе и варьируется от -1 до 1, что позволяет создать симметричную кривую.</li>
     * </ul>
     * В зависимости от флага fill:
     * <ul>
     *     <li>Если fill == true, то парабола будет закрашена случайным цветом;</li>
     *     <li>Если fill == false, то парабола будет нарисована только контуром (без заливки), также случайным цветом.</li>
     * </ul>
     * </p>
     *
     * @param count   количество генерируемых парабол
     * @param fill    флаг, определяющий, будет ли парабола закрашена (true) или нарисована контуром (false)
     * @param scale   коэффициент масштаба, который регулирует размер параболы
     * @param minX    минимальное значение по оси X для размещения центра параболы
     * @param maxX    максимальное значение по оси X для размещения центра параболы
     * @param minY    минимальное значение по оси Y для размещения центра параболы
     * @param maxY    максимальное значение по оси Y для размещения центра параболы
     * @param density плотность распределения точек на холсте, которая влияет на размещение парабол
     */
    private void generateParabolas(int count, boolean fill, double scale, int minX, int maxX, int minY, int maxY, double density) {
        for (int i = 0; i < count; i++) {
            // Центр параболы
            double x = randomCoordinateWithDensity(minX, maxX, density);
            double y = randomCoordinateWithDensity(minY, maxY, density);

            // Учитываем ограничения координат
            double maxWidth = Math.min(random.nextDouble() * 100 * scale, Math.min(x - minX, maxX - x));
            double maxHeight = Math.min(random.nextDouble() * 50 * scale, maxY - y);

            int steps = 100; // Точность параболы
            double[] xPoints = new double[steps + 2];
            double[] yPoints = new double[steps + 2];

            // Генерация точек параболы
            for (int t = 0; t <= steps; t++) {
                double factor = (double) t / steps * 2 - 1; // Коэффициент от -1 до 1
                xPoints[t] = x + maxWidth * factor;

                // Уравнение перевернутой параболы (y = a * x^2 + b * x + c)
                // Для простоты возьмем b = 0, a = -1 для перевернутой параболы.
                yPoints[t] = y - (maxHeight * factor * factor); // Вниз направленная парабола
            }

            // Рисуем параболу
            if (fill) {
                // Закрашиваем параболу
                gc.setFill(randomColor());
                gc.beginPath();
                gc.moveTo(xPoints[0], yPoints[0]);
                for (int j = 1; j <= steps; j++) {
                    gc.lineTo(xPoints[j], yPoints[j]);
                }
                gc.closePath();
                gc.fill();
            } else {
                // Рисуем только контур
                gc.setStroke(randomColor());
                gc.setLineWidth(2);
                gc.beginPath();
                gc.moveTo(xPoints[0], yPoints[0]);
                for (int j = 1; j <= steps; j++) {
                    gc.lineTo(xPoints[j], yPoints[j]);
                }
                gc.stroke();
            }
        }
    }






    /**
     * Генерация случайных трапеций на холсте с возможностью их закрашивания.
     * <p>
     * Метод генерирует трапеции, используя случайные координаты для четырех вершин в пределах заданной области.
     * Как и в случае с другими фигурами, плотность (density) определяет, как часто фигуры будут появляться в различных частях холста.
     * Размеры трапеции регулируются коэффициентом масштаба (scale), а также размерами холста, чтобы фигуры не выходили за его пределы.
     * В зависимости от флага fill, трапеция может быть закрашена или нарисована только контуром.
     * <ul>
     *     <li>Для каждой трапеции генерируются координаты четырех вершин: (x1, y1), (x2, y2), (x3, y3), (x4, y4), где</li>
     *     <ul>
     *         <li>Первая вершина (x1, y1) — это начальная точка, от которой будут отталкиваться остальные вершины;</li>
     *         <li>Вершины (x2, y2) и (x4, y4) будут располагаться на одном уровне по оси Y, образуя верхнюю и нижнюю границы трапеции;</li>
     *         <li>Вершина (x3, y3) будет смещена вниз и может создавать наклонную сторону трапеции.</li>
     *     </ul>
     *     <li>Ширина трапеции и её наклон зависят от случайных значений и масштаба, но также ограничены размерами холста, чтобы фигура помещалась.</li>
     *     <li>Каждая трапеция может быть либо закрашена случайным цветом, если флаг fill равен true, либо нарисована только контуром, если fill равен false.</li>
     * </ul>
     * </p>
     *
     * @param count   количество трапеций для генерации
     * @param fill    флаг, указывающий, следует ли закрашивать трапеции (если true — трапеции будут закрашены, если false — только контур)
     * @param scale   коэффициент масштаба для размеров трапеций
     * @param minX    минимальное значение по оси X для области, в которой будут генерироваться трапеции
     * @param maxX    максимальное значение по оси X для области, в которой будут генерироваться трапеции
     * @param minY    минимальное значение по оси Y для области, в которой будут генерироваться трапеции
     * @param maxY    максимальное значение по оси Y для области, в которой будут генерироваться трапеции
     * @param density плотность генерации, которая влияет на вероятность появления фигур в разных частях холста
     */
    private void generateTrapezoids(int count, boolean fill, double scale, int minX, int maxX, int minY, int maxY, double density) {
        for (int i = 0; i < count; i++) {
            // Центр трапеции
            double x1 = randomCoordinateWithDensity(minX, maxX, density);
            double y1 = randomCoordinateWithDensity(minY, maxY, density);

            // Масштабируем размер трапеции
            double x2 = Math.min(x1 + random.nextDouble() * 60 * scale, maxX);
            double y2 = y1;
            double x3 = Math.min(x1 + random.nextDouble() * 40 * scale, maxX);
            double y3 = Math.min(y1 + random.nextDouble() * 50 * scale, maxY);
            double x4 = Math.max(x1 - random.nextDouble() * 40 * scale, minX);
            double y4 = y3;

            // Если фигура должна быть закрашена
            if (fill) {
                gc.setFill(randomColor());
                gc.fillPolygon(new double[]{x1, x2, x3, x4}, new double[]{y1, y2, y3, y4}, 4);
            } else {
                gc.setStroke(randomColor());
                gc.strokePolygon(new double[]{x1, x2, x3, x4}, new double[]{y1, y2, y3, y4}, 4);
            }
        }
    }


    /**
     * Генерация случайной координаты с учётом плотности распределения.
     * <p>
     * Этот метод генерирует случайную координату в пределах заданного диапазона [min, max],
     * но с учётом плотности (density). Коэффициент плотности влияет на распределение случайных точек.
     * При большем значении плотности координаты будут сконцентрированы ближе к центру диапазона.
     * Для реализации этого используется нормальное распределение, при котором координаты имеют тенденцию
     * скапливаться около среднего значения, а степень разброса зависит от плотности.
     * </p>
     *
     * @param min     минимальная граница диапазона координат
     * @param max     максимальная граница диапазона координат
     * @param density коэффициент плотности, который регулирует "кучность" распределения точек
     * @return случайная координата с учётом плотности
     */
    private double randomCoordinateWithDensity(int min, int max, double density) {
        double center = (min + max) / 2.0;
        double range = (max - min) / (density > 0 ? density : 1); // Чем выше density, тем меньше range
        double offset = random.nextGaussian() * range; // Используем нормальное распределение
        return Math.max(min, Math.min(max, center + offset));
    }

    /**
     * Генерация случайного цвета для рисования.
     * <p>
     * Этот метод генерирует случайный цвет, используя компоненты RGB, значения которых
     * генерируются случайным образом в диапазоне от 0 до 1. Это позволяет создавать
     * разнообразные цвета для различных фигур, добавляя визуальное разнообразие.
     * </p>
     *
     * @return случайный цвет, созданный с помощью случайных значений для красного, зелёного и синего каналов
     */
    private Color randomColor() {
        return Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    /**
     * Подсвечивает область на холсте в заданных координатах.
     * <p>
     * Этот метод используется для визуального выделения прямоугольной области на холсте.
     * Область рисуется с использованием полупрозрачного цвета и обводится синим контуром.
     * Такой эффект может быть полезен для выделения определённых областей или объектов на холсте.
     * </p>
     *
     * @param minX минимальная координата по оси X
     * @param maxX максимальная координата по оси X
     * @param minY минимальная координата по оси Y
     * @param maxY максимальная координата по оси Y
     */
    public void highlightArea(int minX, int maxX, int minY, int maxY, double scale) {
        // Масштабируем координаты и размеры области
        double scaledMinX = minX * scale;
        double scaledMaxX = maxX * scale;
        double scaledMinY = minY * scale;
        double scaledMaxY = maxY * scale;

        // Рисуем закрашенную область
        gc.setFill(Color.LIGHTBLUE.deriveColor(1, 1, 1, 0.3)); // Полупрозрачный цвет
        gc.fillRect(scaledMinX, scaledMinY, scaledMaxX - scaledMinX, scaledMaxY - scaledMinY);

        // Рисуем рамку вокруг области
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeRect(scaledMinX, scaledMinY, scaledMaxX - scaledMinX, scaledMaxY - scaledMinY);
    }
}
