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
    private static final int PEG_RADIUS = 6;
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
        ball.setTranslateX(400);
        ball.setTranslateY(70);

        //new Paths
        Path p1u1 = new Path();
        p1u1.getElements().add(new MoveTo(370, 130));
        p1u1.getElements().add(new LineTo(340, 190));
        p1u1.setStroke(Color.GREY);
        p1u1.setStrokeWidth(2);
        p1u1.setFill(null);

        Path p1u2 = new Path();
        p1u2.getElements().add(new MoveTo(370, 130));
        p1u2.getElements().add(new LineTo(400, 190));
        p1u2.setStroke(Color.GREY);
        p1u2.setStrokeWidth(2);
        p1u2.setFill(null);

        Path p2u1 = new Path();
        p2u1.getElements().add(new MoveTo(340, 190));
        p2u1.getElements().add(new LineTo(310, 250));
        p2u1.setStroke(Color.GREY);
        p2u1.setStrokeWidth(2);
        p2u1.setFill(null);

        Path p2u2 = new Path();
        p2u2.getElements().add(new MoveTo(340, 190));
        p2u2.getElements().add(new LineTo(370, 250));
        p2u2.setStroke(Color.GREY);
        p2u2.setStrokeWidth(2);
        p2u2.setFill(null);

        Path p2u3 = new Path();
        p2u3.getElements().add(new MoveTo(400, 190));
        p2u3.getElements().add(new LineTo(370, 250));
        p2u3.setStroke(Color.GREY);
        p2u3.setStrokeWidth(2);
        p2u3.setFill(null);

        Path p2u4 = new Path();
        p2u4.getElements().add(new MoveTo(400, 190));
        p2u4.getElements().add(new LineTo(430, 250));
        p2u4.setStroke(Color.GREY);
        p2u4.setStrokeWidth(2);
        p2u4.setFill(null);

        Path p3u1 = new Path();
        p3u1.getElements().add(new MoveTo(310, 250));
        p3u1.getElements().add(new LineTo(280, 310));
        p3u1.setStroke(Color.GREY);
        p3u1.setStrokeWidth(2);
        p3u1.setFill(null);

        Path p3u2 = new Path();
        p3u2.getElements().add(new MoveTo(310, 250));
        p3u2.getElements().add(new LineTo(340, 310));
        p3u2.setStroke(Color.GREY);
        p3u2.setStrokeWidth(2);
        p3u2.setFill(null);

        Path p3u3 = new Path();
        p3u3.getElements().add(new MoveTo(370, 250));
        p3u3.getElements().add(new LineTo(340, 310));
        p3u3.setStroke(Color.GREY);
        p3u3.setStrokeWidth(2);
        p3u3.setFill(null);

        Path p3u4 = new Path();
        p3u4.getElements().add(new MoveTo(370, 250));
        p3u4.getElements().add(new LineTo(400, 310));
        p3u4.setStroke(Color.GREY);
        p3u4.setStrokeWidth(2);
        p3u4.setFill(null);

        Path p3u5 = new Path();
        p3u5.getElements().add(new MoveTo(430, 250));
        p3u5.getElements().add(new LineTo(400, 310));
        p3u5.setStroke(Color.GREY);
        p3u5.setStrokeWidth(2);
        p3u5.setFill(null);

        Path p3u6 = new Path();
        p3u6.getElements().add(new MoveTo(430, 250));
        p3u6.getElements().add(new LineTo(460, 310));
        p3u6.setStroke(Color.GREY);
        p3u6.setStrokeWidth(2);
        p3u6.setFill(null);

        Path p4u1 = new Path();
        p4u1.getElements().add(new MoveTo(280, 310));
        p4u1.getElements().add(new LineTo(250, 370));
        p4u1.setStroke(Color.GREY);
        p4u1.setStrokeWidth(2);
        p4u1.setFill(null);

        Path p4u2 = new Path();
        p4u2.getElements().add(new MoveTo(280, 310));
        p4u2.getElements().add(new LineTo(310, 370));
        p4u2.setStroke(Color.GREY);
        p4u2.setStrokeWidth(2);
        p4u2.setFill(null);

        Path p4u3 = new Path();
        p4u3.getElements().add(new MoveTo(340, 310));
        p4u3.getElements().add(new LineTo(310, 370));
        p4u3.setStroke(Color.GREY);
        p4u3.setStrokeWidth(2);
        p4u3.setFill(null);

        Path p4u4 = new Path();
        p4u4.getElements().add(new MoveTo(340, 310));
        p4u4.getElements().add(new LineTo(370, 370));
        p4u4.setStroke(Color.GREY);
        p4u4.setStrokeWidth(2);
        p4u4.setFill(null);

        Path p4u5 = new Path();
        p4u5.getElements().add(new MoveTo(400, 310));
        p4u5.getElements().add(new LineTo(370, 370));
        p4u5.setStroke(Color.GREY);
        p4u5.setStrokeWidth(2);
        p4u5.setFill(null);

        Path p4u6 = new Path();
        p4u6.getElements().add(new MoveTo(400, 310));
        p4u6.getElements().add(new LineTo(430, 370));
        p4u6.setStroke(Color.GREY);
        p4u6.setStrokeWidth(2);
        p4u6.setFill(null);

        Path p4u7 = new Path();
        p4u7.getElements().add(new MoveTo(460, 310));
        p4u7.getElements().add(new LineTo(430, 370));
        p4u7.setStroke(Color.GREY);
        p4u7.setStrokeWidth(2);
        p4u7.setFill(null);

        Path p4u8 = new Path();
        p4u8.getElements().add(new MoveTo(460, 310));
        p4u8.getElements().add(new LineTo(490, 370));
        p4u8.setStroke(Color.GREY);
        p4u8.setStrokeWidth(2);
        p4u8.setFill(null);

        Path p5u1 = new Path();
        p5u1.getElements().add(new MoveTo(250, 370));
        p5u1.getElements().add(new LineTo(220, 430));
        p5u1.setStroke(Color.GREY);
        p5u1.setStrokeWidth(2);
        p5u1.setFill(null);

        Path p5u2 = new Path();
        p5u2.getElements().add(new MoveTo(250, 370));
        p5u2.getElements().add(new LineTo(280, 430));
        p5u2.setStroke(Color.GREY);
        p5u2.setStrokeWidth(2);
        p5u2.setFill(null);

        Path p5u3 = new Path();
        p5u3.getElements().add(new MoveTo(310, 370));
        p5u3.getElements().add(new LineTo(280, 430));
        p5u3.setStroke(Color.GREY);
        p5u3.setStrokeWidth(2);
        p5u3.setFill(null);

        Path p5u4 = new Path();
        p5u4.getElements().add(new MoveTo(310, 370));
        p5u4.getElements().add(new LineTo(340, 430));
        p5u4.setStroke(Color.GREY);
        p5u4.setStrokeWidth(2);
        p5u4.setFill(null);

        Path p5u5 = new Path();
        p5u5.getElements().add(new MoveTo(370, 370));
        p5u5.getElements().add(new LineTo(340, 430));
        p5u5.setStroke(Color.GREY);
        p5u5.setStrokeWidth(2);
        p5u5.setFill(null);

        Path p5u6 = new Path();
        p5u6.getElements().add(new MoveTo(370, 370));
        p5u6.getElements().add(new LineTo(400, 430));
        p5u6.setStroke(Color.GREY);
        p5u6.setStrokeWidth(2);
        p5u6.setFill(null);

        Path p5u7 = new Path();
        p5u7.getElements().add(new MoveTo(430, 370));
        p5u7.getElements().add(new LineTo(400, 430));
        p5u7.setStroke(Color.GREY);
        p5u7.setStrokeWidth(2);
        p5u7.setFill(null);

        Path p5u8 = new Path();
        p5u8.getElements().add(new MoveTo(430, 370));
        p5u8.getElements().add(new LineTo(460, 430));
        p5u8.setStroke(Color.GREY);
        p5u8.setStrokeWidth(2);
        p5u8.setFill(null);

        Path p5u9 = new Path();
        p5u9.getElements().add(new MoveTo(490, 370));
        p5u9.getElements().add(new LineTo(460, 430));
        p5u9.setStroke(Color.GREY);
        p5u9.setStrokeWidth(2);
        p5u9.setFill(null);

        Path p5u10 = new Path();
        p5u10.getElements().add(new MoveTo(490, 370));
        p5u10.getElements().add(new LineTo(520, 430));
        p5u10.setStroke(Color.GREY);
        p5u10.setStrokeWidth(2);
        p5u10.setFill(null);


        //Path Transition
        PathTransition tp1u1 = new PathTransition(Duration.seconds(4), p1u1, ball);
        PathTransition tp1u2 = new PathTransition(Duration.seconds(4), p1u2, ball);
        PathTransition tp2u1 = new PathTransition(Duration.seconds(4), p2u1, ball);
        PathTransition tp2u2 = new PathTransition(Duration.seconds(4), p2u2, ball);
        PathTransition tp2u3 = new PathTransition(Duration.seconds(4), p2u3, ball);
        PathTransition tp2u4 = new PathTransition(Duration.seconds(4), p2u4, ball);
        PathTransition tp3u1 = new PathTransition(Duration.seconds(4), p3u1, ball);
        PathTransition tp3u2 = new PathTransition(Duration.seconds(4), p3u2, ball);
        PathTransition tp3u3 = new PathTransition(Duration.seconds(4), p3u3, ball);
        PathTransition tp3u4 = new PathTransition(Duration.seconds(4), p3u4, ball);
        PathTransition tp3u5 = new PathTransition(Duration.seconds(4), p3u5, ball);
        PathTransition tp3u6 = new PathTransition(Duration.seconds(4), p3u6, ball);
        PathTransition tp4u1 = new PathTransition(Duration.seconds(4), p4u1, ball);
        PathTransition tp4u2 = new PathTransition(Duration.seconds(4), p4u2, ball);
        PathTransition tp4u3 = new PathTransition(Duration.seconds(4), p4u3, ball);
        PathTransition tp4u4 = new PathTransition(Duration.seconds(4), p4u4, ball);
        PathTransition tp4u5 = new PathTransition(Duration.seconds(4), p4u5, ball);
        PathTransition tp4u6 = new PathTransition(Duration.seconds(4), p4u6, ball);
        PathTransition tp4u7 = new PathTransition(Duration.seconds(4), p4u7, ball);
        PathTransition tp4u8 = new PathTransition(Duration.seconds(4), p4u8, ball);
        PathTransition tp5u1 = new PathTransition(Duration.seconds(4), p5u1, ball);
        PathTransition tp5u2 = new PathTransition(Duration.seconds(4), p5u2, ball);
        PathTransition tp5u3 = new PathTransition(Duration.seconds(4), p5u3, ball);
        PathTransition tp5u4 = new PathTransition(Duration.seconds(4), p5u4, ball);
        PathTransition tp5u5 = new PathTransition(Duration.seconds(4), p5u5, ball);
        PathTransition tp5u6 = new PathTransition(Duration.seconds(4), p5u6, ball);
        PathTransition tp5u7 = new PathTransition(Duration.seconds(4), p5u7, ball);
        PathTransition tp5u8 = new PathTransition(Duration.seconds(4), p5u8, ball);
        PathTransition tp5u9 = new PathTransition(Duration.seconds(4), p5u9, ball);
        PathTransition tp5u10 = new PathTransition(Duration.seconds(4), p5u10, ball);



        //tl1.setCycleCount(PathTransition.INDEFINITE);
        //tl1.setAutoReverse(true);
        //tp1u1.setOnFinished(e -> tp1u2.play());
        tp1u1.play();

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
        //Objects in group
        Group paths = new Group(p1u1, p1u2, p2u1, p2u2, p2u3, p2u4, p3u1, p3u2, p3u3, p3u4, p3u5, p3u6, p4u1, p4u2, p4u3, p4u4, p4u5, p4u6, p4u7, p4u8, p5u1, p5u2, p5u3, p5u4, p5u5, p5u6, p5u7, p5u8, p5u9, p5u10);
        Group root = new Group(pane, paths, ball);
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

