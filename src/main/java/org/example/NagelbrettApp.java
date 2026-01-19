package org.example;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.shape.Path;
import javafx.util.Duration;

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

    private static final int PEG_SPACING = 60;
    private static final int PEG_RADIUS = 3;
    int middle = 400;

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
        BorderPane pane = new BorderPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext g = canvas.getGraphicsContext2D();

        //new Ball
        Sphere ball = new Sphere(15);
        ball.setTranslateX(50);
        ball.setTranslateY(50);

        //new Path
        Path path = new Path();
        path.getElements().add(new MoveTo(50, 50));
        path.getElements().add(new CubicCurveTo(150, 0, 250, 200, 350, 100));
        path.setStroke(Color.web("#202124"));
        path.setStrokeWidth(2);
        path.setFill(null);

        PathTransition transition = new PathTransition();
        transition.setNode(ball);
        transition.setPath(path);
        transition.setDuration(Duration.seconds(4));
        transition.setCycleCount(PathTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();

        canvas.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                circles.add(new Circle(e.getX(), e.getY(),
                        10 + rand.nextInt(20),
                        Color.hsb(rand.nextDouble() * 360, 0.7, 0.95)));
            } else if (e.getButton() == MouseButton.SECONDARY) {
                circles.clear();
            }
        });

        pane.setCenter(canvas);
        Group root = new Group(pane, path,  ball);
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
        drawBoxes(g);

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
        // Für ein Quadrat:
        /*for (int y = PEG_SPACING; y < h; y += PEG_SPACING) {
            for (int x = PEG_SPACING; x < w; x += PEG_SPACING) {
                g.fillOval(x - PEG_RADIUS, y - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
            }
        }*/
        int x = 0;
        int x1;
        int y = 0;
        int pin = 1;
        while (pin <= 7 ) {
            x1 = x;
            for (int i = 0; i < pin-1; i++) {

                g.fillOval(400 + x1 * PEG_SPACING/2 - PEG_RADIUS, 70 + y * PEG_SPACING - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
                x1 = x1 + 2;
            }
            y = y + 1;
            x = x - 1;
            pin = pin + 1;


        }
        //Herangehensweise der Formel oben:
        /* g.fillOval(400 + 0 * 20 - PEG_RADIUS, 100 + 0 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);

        g.fillOval(400 + (-1) * 20 - PEG_RADIUS, 100 + 1 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 1 * 20 - PEG_RADIUS, 100 + 1 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);

        g.fillOval(400 + (-2) * 20 - PEG_RADIUS, 100 + 2 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 0 * 20 - PEG_RADIUS, 100 + 2 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 2 * 20 - PEG_RADIUS, 100 + 2 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);

        g.fillOval(400 + (-3) * 20 - PEG_RADIUS, 100 + 3 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + (-1) * 20 - PEG_RADIUS, 100 + 3 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 1 * 20  - PEG_RADIUS, 100 + 3 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 3 * 20 - PEG_RADIUS, 100 + 3 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);

        g.fillOval(400 + (-4) * 20 - PEG_RADIUS, 100 + 4 * 40- PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + (-2) * 20 - PEG_RADIUS, 100 + 4 * 40  - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 0 * 20 - PEG_RADIUS, 100 + 4 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 2 * 20 - PEG_RADIUS, 100 + 4 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 4 * 20 - PEG_RADIUS, 100 + 4 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);

        g.fillOval(400 + (-5) * 20 - PEG_RADIUS, 100 + 5 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + (-3) * 20 - PEG_RADIUS, 100 + 5 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + (-1) * 20 - PEG_RADIUS, 100 + 5 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 1 * 20 - PEG_RADIUS, 100 + 5 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 3 * 20 - PEG_RADIUS, 100 + 5 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0);
        g.fillOval(400 + 5 * 20 - PEG_RADIUS, 100 + 5 * 40 - PEG_RADIUS, PEG_RADIUS * 2.0, PEG_RADIUS * 2.0); */


    }
    private void drawBoxes(GraphicsContext g) {
        g.setFill(Color.web("#3d1b5e"));
        g.setStroke(Color.web("#ad83d6"));
        g.setLineWidth(3);

        //g.fillRoundRect(75, 75, 90, 60, 16, 16);
        //g.strokeRoundRect(75, 75, 90, 60, 16, 16);

        g.fillRoundRect(190, 455, 60, 42, 16, 16);
        g.strokeRoundRect(190, 455, 60, 42, 16, 16);
        g.setStroke(Color.web("#202124"));
        g.strokeLine(200, 455, 240, 455);

        g.setStroke(Color.web("#ad83d6"));
        g.fillRoundRect(250, 455, 60, 42, 16, 16);
        g.strokeRoundRect(250, 455, 60, 42, 16, 16);
        g.setStroke(Color.web("#202124"));
        g.strokeLine(260, 455, 300, 455);

        g.setStroke(Color.web("#ad83d6"));
        g.fillRoundRect(310, 455, 60, 42, 16, 16);
        g.strokeRoundRect(310, 455, 60, 42, 16, 16);
        g.setStroke(Color.web("#202124"));
        g.strokeLine(320, 455, 360, 455);

        g.setStroke(Color.web("#ad83d6"));
        g.fillRoundRect(370, 455, 60, 42, 16, 16);
        g.strokeRoundRect(370, 455, 60, 42, 16, 16);
        g.setStroke(Color.web("#202124"));
        g.strokeLine(380, 455, 420, 455);

        g.setStroke(Color.web("#ad83d6"));
        g.fillRoundRect(430, 455, 60, 42, 16, 16);
        g.strokeRoundRect(430, 455, 60, 42, 16, 16);
        g.setStroke(Color.web("#202124"));
        g.strokeLine(440, 455, 480, 455);

        g.setStroke(Color.web("#ad83d6"));
        g.fillRoundRect(490, 455, 60, 42, 16, 16);
        g.strokeRoundRect(490, 455, 60, 42, 16, 16);
        g.setStroke(Color.web("#202124"));
        g.strokeLine(500, 455, 540, 455);
    }

    private void drawExamples(GraphicsContext g) {
        g.setFill(Color.web("#3d1b5e"));
        g.setStroke(Color.web("#ad83d6"));
        g.setLineWidth(3);
        g.fillRoundRect(75, 75, 90, 60, 16, 16);
        g.strokeRoundRect(75, 75, 90, 60, 16, 16);

        g.setStroke(Color.web("#ed5439"));
        g.setLineWidth(2);
        double x1 = 80, y1 = 120, x2 = 160, y2 = 120;
        g.strokeLine(x1, y1, x2, y2);
        drawArrowHead(g, x1, y1, x2, y2);

        g.setFill(Color.WHITE);
        g.setFont(Font.font(15));
        g.fillText("Hello world", 80, 105);
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

