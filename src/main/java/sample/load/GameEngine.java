package sample.load;

import javafx.scene.input.KeyCode;
import sample.dataoperation.GameLogger;
import sample.dataoperation.UserStatistic;
import sample.ui.GameGrid;

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GameEngine {

    private static final String GAME_NAME = "BestSokobanEverV6";
    private static GameLogger m_logger;
    private static boolean debug = false;
    private Level m_currentLevel;
    private String m_mapSetName;
    private List<Level> m_levels_list;
    private boolean gameComplete = false;
    private int movesCount = 0;


    public GameEngine(InputStream input, boolean production) {
        try {
            m_logger = new GameLogger();
            m_levels_list = LoadGameFile(input);
            m_currentLevel = GetNextLevel();
            UserStatistic.Initialize_score();
            UserStatistic.Init_consumption();
            UserStatistic.Start_time();

            if (production) {
                createPlayer();
            }
        } catch (IOException x) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            m_logger.warning("Cannot load the default save file: " + e.getStackTrace());
        } catch (LineUnavailableException e) {
            m_logger.warning("Cannot load the music file: " + e.getStackTrace());
        }
    }

    public static boolean IsDebugActive() {
        return debug;
    }
    public static GameLogger GetM_logger() {
        return m_logger;
    }

    public static String GetGameName() {
        return GAME_NAME;
    }

    public void HandleKey(KeyCode code) throws IOException {
        switch (code) {
            case UP:
                Move(new Point(-1, 0));
                break;

            case RIGHT:
                Move(new Point(0, 1));
                break;

            case DOWN:
                Move(new Point(1, 0));
                break;

            case LEFT:
                Move(new Point(0, -1));
                break;

            default:
                // TODO: implement something funny.
        }

        if (IsDebugActive()) {
            System.out.println(code);
        }
    }

    public void Move(Point delta) throws IOException {
        if (IsGameComplete()) {
            return;
        }

        Point keeperPosition = m_currentLevel.GetKeeperPosition();
        GameObject keeper = m_currentLevel.GetObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        GameObject keeperTarget = m_currentLevel.GetObjectAt(targetObjectPoint);

        if (GameEngine.IsDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(m_currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf("Target object: %s at [%s]", keeperTarget, targetObjectPoint);
        }

        boolean keeperMoved = false;

        switch (keeperTarget) {

            case WALL:
                break;

            case CRATE:

                GameObject crateTarget = m_currentLevel.GetTargetObject(targetObjectPoint, delta);
                if (crateTarget != GameObject.FLOOR) {
                    break;
                }

                m_currentLevel.MoveGameObjectBy(keeperTarget, targetObjectPoint, delta);
                m_currentLevel.MoveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            case FLOOR:
                m_currentLevel.MoveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            default:
                m_logger.severe("The object to be moved was not a recognised GameObject.");
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
        }

        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            UserStatistic.AddMoves_consumption();
            if (m_currentLevel.IsComplete()) {
                if (IsDebugActive()) {
                    System.out.println("Level complete!");
                }
                //Current score

                UserStatistic.set_print_write_Score();
                UserStatistic.AddLevel();

                m_currentLevel = GetNextLevel();
            }
        }
    }




    public List<Level> LoadGameFile(InputStream input) {
        List<Level> levels = new ArrayList<>(5);
        int levelIndex = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
            String levelName = "";

            while (true) {
                String line = reader.readLine();

                // Break the loop if EOF is reached
                if (line == null) {
                    if (rawLevel.size() != 0) {
                        Level parsedLevel = new Level(levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                    }
                    break;
                }

                if (line.contains("MapSetName")) {
                    m_mapSetName = line.replace("MapSetName: ", "");
                    continue;
                }

                if (line.contains("LevelName")) {
                    if (parsedFirstLevel) {
                        Level parsedLevel = new Level(levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                        rawLevel.clear();
                    } else {
                        parsedFirstLevel = true;
                    }

                    levelName = line.replace("LevelName: ", "");
                    continue;
                }

                line = line.trim();
                line = line.toUpperCase();
                // If the line contains at least 2 WALLS, add it to the list
                if (line.matches(".*W.*W.*")) {
                    rawLevel.add(line);
                }
            }

        } catch (IOException e) {
            m_logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            m_logger.severe("Cannot open the requested file: " + e);
        }

        return levels;
    }

    public boolean IsGameComplete() {
        return gameComplete;
    }

    public void createPlayer() throws LineUnavailableException {
//        File filePath = new File(getClass().getClassLoader().getResource("music/puzzle_theme.wav").toString());
//        Media music = new Media(filePath.toURI().toString());
//        player = new MediaPlayer(music);
//        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
    }

    public void playMusic() {
//        player.play();
    }

    public void stopMusic() {
//        player.stop();
    }

    public boolean isPlayingMusic() {
//        return player.getStatus() == MediaPlayer.Status.PLAYING;
        return false;
    }

    public Level GetNextLevel() {
        if (m_currentLevel == null) {
            return m_levels_list.get(0);
        }

        int currentLevelIndex = m_currentLevel.GetIndex();
        if (currentLevelIndex < m_levels_list.size()) {
            return m_levels_list.get(currentLevelIndex + 1);
        }

        gameComplete = true;
        return null;
    }

    public Level GetCurrentLevel() {
        return m_currentLevel;
    }

    public void ToggleDebug() {
        debug = !debug;
    }

}