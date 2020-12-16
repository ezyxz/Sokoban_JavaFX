package sample.scene;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * @author Xinyuan Zuo
 * This class is to realize START screen so please run from here
 * The BGM will run automaticlly
 */
public class Main extends Application {

    private static Stage m_primaryStage;
    private static MediaPlayer player;
    public static void main(String[] args) {
        launch(args);
    }

    public static MediaPlayer GetPlayer() {
        return player;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(this.getClass().getResource(""));
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Game");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        this.m_primaryStage=primaryStage;
        //Audio
        Media media = new Media(this.getClass().getResource("puzzle_theme.wav").toString());
        player = new MediaPlayer(media);
        player.play();


    }
    public static void Close_main(){
        m_primaryStage.close();
    }

}
