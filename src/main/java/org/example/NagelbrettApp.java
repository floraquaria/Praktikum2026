package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Nagelbrett (pegboard) demo on a JavaFX Canvas.
 * - Draws a grid of pegs (dots)
 * - Animates a bouncing ball with gravity
 * - Left-click: add colored circles; Right-click: clear
 * - Short & commented for pupils learning Java
 */
public class NagelbrettApp extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;

    private static final int PEG_SPACING = 40;
    private static final int PEG_RADIUS = 3;

    private double ballX = 120;
    private double ballY = 80;
    private double ballVX = 180;
    private double ballVY = 0;
    private double ballRadius = 12;
    private double gravity = 450;
    private double bounce = 0.72;

    private final List<Circle> circles = new ArrayList<>();
    private final Random rand = new Random();
    private long lastNanos = 0;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext g = canvas.getGraphicsContext2D();

        canvas.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                circles.add(new Circle(e.getX(), e.getY(),
                        10 + rand.nextInt(20),
                        Color.hsb(rand.nextDouble() * 360, 0.7, 0.95)));
            } else if (e.getButton() == MouseButton.SECONDARY) {
                circles.clear();
            }
        });

        root.setCenter(canvas);

        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.web("#202124"));
        stage.setTitle("JavaFX Canvas: Nagelbrett + Animation (Starter)");
        stage.setScene(scene);
        stage.show();

        // Optional: make canvas follow window size
        scene.widthProperty().addListener((obs, o, w) -> canvas.setWidth(w.doubleValue()));
        scene.heightProperty().addListener((obs, o, h) -> canvas.setHeight(h.doubleValue()));

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastNanos == 0) {
                    lastNanos = now;
                    return;
                }
                double dt = (now - lastNanos) / 1_000_000_000.0;
                lastNanos = now;

                updatePhysics(dt, canvas);
                draw(g, canvas);
            }
        }.start();
    }

    private void updatePhysics(double dt, Canvas canvas) {
        ballVY += gravity * dt;
        ballX += ballVX * dt;
        ballY += ballVY * dt;

        double w = canvas.getWidth();
        double h = canvas.getHeight();

        if (ballX - ballRadius < 0) {
            ballX = ballRadius;
            ballVX = -ballVX * bounce;
        } else if (ballX + ballRadius > w) {
            ballX = w - ballRadius;
            ballVX = -ballVX * bounce;
        }

        if (ballY - ballRadius < 0) {
            ballY = ballRadius;
            ballVY = -ballVY * bounce;
        } else if (ballY + ballRadius > h) {
            ballY = h - ballRadius;
            ballVY = -ballVY * bounce;
            if (Math.abs(ballVY) < 30) ballVY = 0;
        }

        // Shrink user circles slowly; remove when tiny
        Iterator<Circle> it = circles.iterator();
        while (it.hasNext()) {
            Circle c = it.next();
            c.r -= dt * 3;
            if (c.r <= 3) it.remove();
        }
    }

    private void draw(GraphicsContext g, Canvas canvas) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        g.setFill(Color.web("#202124"));
        g.fillRect(0, 0, w, h);

        g.setFill(Color.WHITE);
        g.setFont(Font.font(16));
        g.fillText("Nagelbrett • Links-Klick: Kreis hinzufügen • Rechts-Klick: löschen • JavaFX Canvas",
                12, 24);

        drawPegboard(g, w, h);
        drawExamples(g);

        for (Circle c : circles) {
            g.setFill(c.color);
            g.fillOval(c.x - c.r, c.y - c.r, c.r * 2, c.r * 2);
        }

        g.setFill(Color.CORNFLOWERBLUE);
        g.fillOval(ballX - ballRadius, ballY - ballRadius, ballRadius * 2, ballRadius * 2);

        double shadowY = Math.min(h - 8, ballY + ballRadius + 8);
        double shadowScale = Math.max(0.2, 1.0 - (Math.abs(shadowY - (h - ballRadius)) / h));
        g.setGlobalAlpha(0.25);
        g.setFill(Color.BLACK);
        g.fillOval(ballX - ballRadius * shadowScale, shadowY,
                (ballRadius * 2) * shadowScale, ballRadius * shadowScale);
        g.setGlobalAlpha(1.0);
    }

    private void drawPegboard(GraphicsContext g, double w, double h) {
        g.setFill(Color.web("#a0a0a0"));
        for (int y = PEG_SPACING; y < h; y += PEG_SPACING) {
            for (int x = PEG_SPACING; x < w; x += PEG_SPACING) {
                g.fillOval(x - PEG_RADIUS, y - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
            }
        }
    }

    private void drawExamples(GraphicsContext g) {
        g.setFill(Color.web("#00C853"));
        g.setStroke(Color.web("#1B5E20"));
        g.setLineWidth(3);
        g.fillRoundRect(500, 60, 120, 60, 16, 16);
        g.strokeRoundRect(500, 60, 120, 60, 16, 16);

        g.setStroke(Color.ORANGE);
        g.setLineWidth(2);
        double x1 = 520, y1 = 150, x2 = 640, y2 = 210;
        g.strokeLine(x1, y1, x2, y2);
        drawArrowHead(g, x1, y1, x2, y2);

        g.setFill(Color.WHITE);
        g.setFont(Font.font(14));
        g.fillText("Beispiel: Rechteck, Linie, Text, Animation", 500, 240);
    }

    private void drawArrowHead(GraphicsContext g, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double len = 10;
        double a1 = angle - Math.toRadians(25);
        double a2 = angle + Math.toRadians(25);

        double xA = x2 - len * Math.cos(a1);
        double yA = y2 - len * Math.sin(a1);
        double xB = x2 - len * Math.cos(a2);
        double yB = y2 - len * Math.sin(a2);

        g.strokeLine(x2, y2, xA, yA);
        g.strokeLine(x2, y2, xB, yB);
    }

    private static class Circle {
        double x, y, r;
        Color color;
        Circle(double x, double y, double r, Color color) {
            this.x = x; this.y = y; this.r = r; this.color = color;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

