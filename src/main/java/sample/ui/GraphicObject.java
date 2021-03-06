package sample.ui;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import sample.load.GameEngine;
import sample.load.GameObject;

public class GraphicObject extends Rectangle {



    public GraphicObject(GameObject obj) {

        Paint color;
        switch (obj) {
            case WALL:
                color = BackgroundColor.GetSelect_color();
                break;

            case CRATE:
                color = Color.ORANGE;
                break;

            case DIAMOND:
                color = Color.DEEPSKYBLUE;

                // TODO: fix memory leak.
                if (GameEngine.IsDebugActive()) {
                    FadeTransition ft = new FadeTransition(Duration.millis(1000), this);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.2);
                    ft.setCycleCount(Timeline.INDEFINITE);
                    ft.setAutoReverse(true);
                    ft.play();
                }

                break;

            case KEEPER:
                color = Color.RED;
                break;

            case FLOOR:
                color = Color.WHITE;
                break;

            case CRATE_ON_DIAMOND:
                color = Color.DARKCYAN;
                break;

            default:
                String message = "Error in Level constructor. Object not recognized.";
                GameEngine.GetM_logger().severe(message);
                throw new AssertionError(message);
        }

        this.setFill(color);
        this.setHeight(30);
        this.setWidth(30);

        if (obj != GameObject.WALL) {
            this.setArcHeight(50);
            this.setArcWidth(50);
        }

        if (GameEngine.IsDebugActive()) {
            this.setStroke(Color.RED);
            this.setStrokeWidth(0.25);
        }
    }

}
