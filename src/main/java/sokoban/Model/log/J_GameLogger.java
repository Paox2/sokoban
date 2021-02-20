package sokoban.Model.log;

import sokoban.Model.game.J_GameEngine;
import sokoban.Model.gameEngine.music.J_MusicSingleton;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *  <p>
 *      Print the logger message if the debug pattern is open.
 *  </p>
 *
 * @author Wendi Han
 * @see Logger#info(String)
 * @see Logger#warning(String)
 * @see Logger#severe(String)
 * @see File#mkdir()
 * @see FileHandler
 * @version 1.1
 */
public class J_GameLogger extends Logger {

    private final Logger m_Logger = Logger.getLogger("GameLogger");
    private final DateFormat m_DateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Calendar m_Calendar = Calendar.getInstance();
    private volatile static J_GameLogger instance = null;

    /**
     * Get the instance of game logger.
     *
     * @return Game Logger
     */
    public static J_GameLogger getInstance(){
        if(instance == null) {
            synchronized (J_GameLogger.class) {
                if(instance == null) {
                    try {
                        instance = new J_GameLogger();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Connect to the file to save the log message.
     *
     * @throws IOException - Detect exception when load file.
     * @since 1.1
     * @see File#mkdir()
     * @see FileHandler
     */
    private J_GameLogger() throws IOException {
        super("GameLogger", null);

        File directory = new File(System.getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(directory + "/sokoban.log");
        m_Logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    /**
     * Use format to unify the message.
     *
     * @param message Message
     * @return String  Organized message
     */
    private String createFormattedMessage(String message) {
        return m_DateFormat.format(m_Calendar.getTime()) + " -- " + message;
    }

    /**
     * @param message Organized message
     * @see Logger#info(String) 
     */
    public void info(String message) {
        m_Logger.info(createFormattedMessage(message));
    }

    /**
     * @param message  Organized message
     * @see Logger#warning(String) 
     */
    public void warning(String message) {
        m_Logger.warning(createFormattedMessage(message));
    }

    /**
     * @param message  Organized message
     * @see Logger#severe(String)
     */
    public void severe(String message) {
        m_Logger.severe(createFormattedMessage(message));
    }
}