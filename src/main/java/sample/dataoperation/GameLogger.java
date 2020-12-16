package sample.dataoperation;

import sample.load.GameEngine;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger extends Logger {

    private static Logger m_logger = Logger.getLogger("GameLogger");
    private DateFormat m_dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Calendar m_calendar = Calendar.getInstance();

    public GameLogger() throws IOException {
        super("GameLogger", null);

        File directory = new File(System.getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(directory + "/" + GameEngine.GetGameName() + ".log");
        m_logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    private String CreateFormattedMessage(String message) {
        return m_dateFormat.format(m_calendar.getTime()) + " -- " + message;
    }

    public void info(String message) {
        m_logger.info(CreateFormattedMessage(message));
    }

    public void warning(String message) {
        m_logger.warning(CreateFormattedMessage(message));
    }

    public void severe(String message) {
        m_logger.severe(CreateFormattedMessage(message));
    }
}