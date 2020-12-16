package sample.scene;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.dataoperation.ScoreRecord;
import sample.dataoperation.UserStatistic;
import sample.load.GameEngine;
import sample.load.GameObject;
import sample.load.Level;
import sample.ui.GraphicObject;

import java.awt.*;
import java.io.*;
import java.util.Optional;

/**
 * @author Xinyuan Zuo-modified
 * This class is to run the game
 */

public class APP extends Application {

    private  static Stage m_primaryStage;
    private GameEngine m_gameEngine;
    private GridPane m_gameGrid;
    private File m_saveFile;
    private MenuBar m_MENU;

    public static void main(String[] args) {
        launch(args);
        System.out.println("Done!");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.m_primaryStage = primaryStage;

        GridPane root = create_GridPane();
        primaryStage.setTitle(GameEngine.GetGameName());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        loadDefaultSaveFile(primaryStage);
        // When user close the window It will info user if record his score
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                alert2.setTitle("Exit");
                alert2.setHeaderText("Are you sure to exit with recording your game level");
                Optional<ButtonType> result = alert2.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    primaryStage.close();
                    ScoreRecord.Write(UserStatistic.GetUserName(),UserStatistic.GetCurrent_score());

                } else {
                    primaryStage.close();
                }
            }
        });

    }
    private GridPane create_GridPane(){
        m_MENU = new MenuBar();

        MenuItem menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setDisable(true);
        menuItemSaveGame.setOnAction(actionEvent -> saveGame());
        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(actionEvent -> loadGame());
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(actionEvent -> closeGame());
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(menuItemSaveGame, menuItemLoadGame, new SeparatorMenuItem(), menuItemExit);

        MenuItem menuItemUndo = new MenuItem("Undo");
        menuItemUndo.setDisable(true);
        menuItemUndo.setOnAction(actionEvent -> undo());
        RadioMenuItem radioMenuItemMusic = new RadioMenuItem("Toggle Music");
        radioMenuItemMusic.setOnAction(actionEvent -> toggleMusic());
        RadioMenuItem radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        radioMenuItemDebug.setOnAction(actionEvent -> toggleDebug());
        MenuItem menuItemResetLevel = new MenuItem("Reset Level");
        Menu menuLevel = new Menu("Level");
        menuLevel.setOnAction(actionEvent -> resetLevel());
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic, radioMenuItemDebug,
                new SeparatorMenuItem(), menuItemResetLevel);

        MenuItem menuItemGame = new MenuItem("About This Game");
        Menu menuAbout = new Menu("About");
        menuAbout.setOnAction(actionEvent -> showAbout());
        menuAbout.getItems().addAll(menuItemGame);
        //ViewRank
        MenuItem RankView = new MenuItem("View Rank");
        Menu Rank = new Menu("Rank");
        Rank.setOnAction(actionEvent -> {
            try {
                viewRank();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Rank.getItems().addAll(RankView);

        m_MENU.getMenus().addAll(menuFile, menuLevel, menuAbout,Rank);
        m_gameGrid = new GridPane();
        GridPane root = new GridPane();
        root.add(m_MENU, 0, 0);
        root.add(m_gameGrid, 0, 1);
        return root;
    }

    private void viewRank() throws IOException {
        StringBuffer info = ScoreRecord.Read_rank(UserStatistic.GetLevel());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("ALERT");
        alert.headerTextProperty().set("User Record");
        alert.setContentText(info.toString());
        alert.show();
    }

    private void loadDefaultSaveFile(Stage primaryStage) {
        this.m_primaryStage = primaryStage;
        System.out.println();
//        InputStream in = getClass().getClassLoader().getResourceAsStream("/level/SampleGame.skb");
        InputStream in = getClass().getResourceAsStream("/level/SampleGame.skb");

        InitializeGame(in);
        setEventFilter();
    }

    private void InitializeGame(InputStream input) {
        m_gameEngine = new GameEngine(input, true);
        reloadGrid();
    }

    private void setEventFilter() {
        m_primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            try {
                m_gameEngine.HandleKey(event.getCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
            reloadGrid();
        });}
    private void loadGameFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        m_saveFile = fileChooser.showOpenDialog(m_primaryStage);

        if (m_saveFile != null) {
            if (GameEngine.IsDebugActive()) {
                GameEngine.GetM_logger().info("Loading save file: " + m_saveFile.getName());
            }
            InitializeGame(new FileInputStream(m_saveFile));
        }
    }

        private void reloadGrid() {
        if (m_gameEngine.IsGameComplete()) {
            return;
        }

        Level currentLevel = m_gameEngine.GetCurrentLevel();
        Level.LevelIterator levelGridIterator = (Level.LevelIterator) currentLevel.iterator();
            m_gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition());
        }m_gameGrid.autosize();
            m_primaryStage.sizeToScene();
    }


    private void newDialog(String dialogTitle, String dialogMessage, Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(m_primaryStage);
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);

        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(javafx.scene.text.Font.font(14));

        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text1);

        Scene dialogScene = new Scene(dialogVbox, 350, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = new GraphicObject(gameObject);
        m_gameGrid.add(graphicObject, location.y, location.x);
    }

    private void closeGame() {
        System.exit(0);
    }
    private void saveGame() {
    }
    private void loadGame() {
        try {
            loadGameFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void undo() { closeGame(); }
    private void resetLevel() {}

    private void showAbout() {
        String title = "About This Game";
        String message = "Enjoy the Game!\n";
        newDialog(title, message, null);
    }
    private void toggleMusic() {
        // TODO
    }
    private void toggleDebug() {
        m_gameEngine.ToggleDebug();
        reloadGrid();
    }

}

