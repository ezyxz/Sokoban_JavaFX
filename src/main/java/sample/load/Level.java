package sample.load;

import sample.ui.GameGrid;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import static sample.ui.GameGrid.translatePoint;


public final class Level implements Iterable<GameObject> {

    private final String m_name;
    private final GameGrid m_objectsGrid;
    private final GameGrid m_diamondsGrid;
    private final int index;
    private int numberOfDiamonds = 0;
    private Point m_keeperPosition = new Point(0, 0);

    public Level(String levelName, int levelIndex, List<String> raw_level) {
        if (GameEngine.IsDebugActive()) {
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
        }

        m_name = levelName;
        index = levelIndex;

        int rows = raw_level.size();
        int columns = raw_level.get(0).trim().length();

        m_objectsGrid = new GameGrid(rows, columns);
        m_diamondsGrid = new GameGrid(rows, columns);

        for (int row = 0; row < raw_level.size(); row++) {

            // Loop over the string one char at a time because it should be the fastest way:
            // http://stackoverflow.com/questions/8894258/fastest-way-to-iterate-over-all-the-chars-in-a-string
            for (int col = 0; col < raw_level.get(row).length(); col++) {
                // The game object is null when the we're adding a floor or a diamond
                GameObject curTile = GameObject.fromChar(raw_level.get(row).charAt(col));

                if (curTile == GameObject.DIAMOND) {
                    numberOfDiamonds++;
                    m_diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = GameObject.FLOOR;
                } else if (curTile == GameObject.KEEPER) {
                    m_keeperPosition = new Point(row, col);
                }

                m_objectsGrid.putGameObjectAt(curTile, row, col);
                curTile = null;
            }
        }
    }

    public boolean IsComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < m_objectsGrid.GetROWS(); row++) {
            for (int col = 0; col < m_objectsGrid.GetCOLUMNS(); col++) {
                if (m_objectsGrid.getGameObjectAt(col, row) == GameObject.CRATE && m_diamondsGrid.getGameObjectAt(col, row) == GameObject.DIAMOND) {
                    cratedDiamondsCount++;
                }
            }
        }

        return cratedDiamondsCount >= numberOfDiamonds;
    }

    public String getName() {
        return m_name;
    }

    public int GetIndex() {
        return index;
    }

    public Point GetKeeperPosition() {
        return m_keeperPosition;
    }

    public GameObject GetTargetObject(Point source, Point delta) {
        return m_objectsGrid.getTargetFromSource(source, delta);
    }

    public GameObject GetObjectAt(Point p) {
        return m_objectsGrid.getGameObjectAt(p);
    }

    public void MoveGameObjectBy(GameObject object, Point source, Point delta) {
        MoveGameObjectTo(object, source, translatePoint(source, delta));
    }

    public void MoveGameObjectTo(GameObject object, Point source, Point destination) {
        m_objectsGrid.putGameObjectAt(GetObjectAt(destination), source);
        m_objectsGrid.putGameObjectAt(object, destination);
    }

    @Override
    public String toString() {
        return m_objectsGrid.toString();
    }

    @Override
    public Iterator<GameObject> iterator() {
        return new LevelIterator();
    }

    public class LevelIterator implements Iterator<GameObject> {

        int column = 0;
        int row = 0;

        @Override
        public boolean hasNext() {
            return !(row == m_objectsGrid.GetROWS() - 1 && column == m_objectsGrid.GetCOLUMNS());
        }

        @Override
        public GameObject next() {
            if (column >= m_objectsGrid.GetCOLUMNS()) {
                column = 0;
                row++;
            }

            GameObject object = m_objectsGrid.getGameObjectAt(column, row);
            GameObject diamond = m_diamondsGrid.getGameObjectAt(column, row);
            GameObject retObj = object;

            column++;

            if (diamond == GameObject.DIAMOND) {
                if (object == GameObject.CRATE) {
                    retObj = GameObject.CRATE_ON_DIAMOND;
                } else if (object == GameObject.FLOOR) {
                    retObj = diamond;
                } else {
                    retObj = object;
                }
            }

            return retObj;
        }

        public Point getCurrentPosition() {
            return new Point(column, row);
        }
    }
}