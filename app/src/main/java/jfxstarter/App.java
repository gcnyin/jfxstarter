package jfxstarter;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class App extends Application {
    static final double FLAKE_IMG_HEIGHT = 5;

    static final Image FLAKE_IMG = new Image(Objects.requireNonNull(App.class.getResource("/flake.png")).toExternalForm(), FLAKE_IMG_HEIGHT, FLAKE_IMG_HEIGHT, true, true);

    static class Flake extends ImageView {
        private double verSpeed;
        private double horSpeed;

        public Flake() {
            super(FLAKE_IMG);
        }

        public double getVerSpeed() {
            return verSpeed;
        }

        public void setVerSpeed(double verSpeed) {
            this.verSpeed = verSpeed;
        }

        public double getHorSpeed() {
            return horSpeed;
        }

        public void setHorSpeed(double horSpeed) {
            this.horSpeed = horSpeed;
        }
    }

    private final List<Flake> flakes = new ArrayList<>(2000);
    private final Pane root = new Pane();
    private double w;
    private double h;
    private final Random random = new Random();

    private final Text fpsText = new Text("FPS: 0");

    private static final long ONE_SECOND = 1_000_000_000; // 1s = 10亿nano

    @Override
    public void start(Stage primaryStage) throws Exception {
        fpsText.setFill(Color.WHITE);
        fpsText.setFont(Font.font(35));
        fpsText.setTextOrigin(VPos.TOP);
        root.getChildren().add(fpsText);
        Image background = new Image(Objects.requireNonNull(App.class.getResource("/b3.jpeg")).toExternalForm());
        h = background.getHeight() - FLAKE_IMG_HEIGHT;
        w = background.getWidth() - FLAKE_IMG_HEIGHT;
        root.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        initShow();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("2021-11-03 20:23");
        primaryStage.setResizable(false);
        primaryStage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            int times;
            long lastTime = System.nanoTime();
            double duration;
            int fps;
            boolean reverse;

            @Override
            public void handle(long now) {
                fps++;
                duration += now - lastTime;
                if (duration >= ONE_SECOND) {
                    fpsText.setText("FPS: " + fps);
                    fps = 0;
                    duration = 0;
                }
                lastTime = now;

                times++;
                if (times % 180 == 0) {
                    reverse = true;
                    times = 0;
                }
                for (Flake flake : flakes) {
                    // flake.setRotate((flake.getRotate() + 3) % 360);
                    flake.setY(flake.getY() + flake.getVerSpeed());
                    if (reverse) {
                        flake.setHorSpeed(flake.getHorSpeed() * -1);
                    }
                    flake.setX(flake.getX() + flake.getHorSpeed());
                    if (flake.getY() > h) {
                        flake.setX(random.nextDouble() * w);
                        flake.setY(0);
                        flake.setVerSpeed(random.nextDouble() * 3 + 0.5);
                        flake.setScaleX(random.nextDouble() * 0.5 + 0.7);
                        flake.setScaleY(flake.getScaleY());
                    }
                    flake.setOpacity(1 - flake.getY() / h);
                }
                reverse = false;
            }
        };

        animationTimer.start();
    }

    private void initShow() {
        for (int i = 0; i < 2000; i++) {
            Flake flake = new Flake();
            flake.setX(random.nextDouble() * w);
            flake.setY(random.nextDouble() * h);
            flake.setVerSpeed(random.nextDouble() * 3 + 0.5);
            flake.setScaleX(random.nextDouble() * 0.5 + 0.7);
            flake.setScaleY(flake.getScaleY());
            flake.setOpacity(1 - flake.getY() / h);
            flake.setHorSpeed(random.nextDouble() - 0.5);
            // flake.setRotate((flake.getRotate() + 3) % 360);

            flakes.add(flake);
        }
        root.getChildren().addAll(flakes);
    }
}
