package sokoban.Model.gameEngine.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 *  <p>
 *      This class is used to open or close music and set the volume of music.<br>
 *      {@code toggleMusic()} will get the state of music and decide to open it or not.<br>
 *      {@code setVolumn(Double value)} set the volumn of music.
 *  </p>
 *
 * @author Wendi Han - edited
 * @version 1.1
 * @see MediaPlayer#setOnEndOfMedia(Runnable)
 * @see MediaPlayer#play()
 * @see MediaPlayer#setVolume(double)
 */
public class J_MusicSingleton {
    private MediaPlayer m_Player;
    private Double m_Volume;
    private volatile static J_MusicSingleton instance = null;

    /**
     * Constructor is used to call the {@code createPlayer()} to initialize the music player.
     *
     * @since 1.1
     */
    private J_MusicSingleton() {
        createPlayer();
    }

    /**
     * Get the instance of music.
     *
     * @return Music controller
     */
    public static J_MusicSingleton getInstance(){
        if(instance == null) {
            synchronized (J_MusicSingleton.class) {
                if(instance == null) {
                    instance = new J_MusicSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Use singleton pattern to initialize the music and set the volume to 0.2.
     *
     * @see MediaPlayer#setOnEndOfMedia(Runnable)
     * @since 1.1
     */
    public void createPlayer(){
        Media music = new Media(new File("src/main/resources/MUSIC/puzzle_theme.wav").toURI().toString());
        m_Player = new MediaPlayer(music);
        m_Player.setOnEndOfMedia(() -> m_Player.seek(Duration.ZERO));
        m_Volume = 0.2;
    }

    /**
     * Play the music and set the initial volume to the value of volume bar at scene.
     *
     * @see MediaPlayer#play()
     * @see MediaPlayer#setVolume(double)
     * @since 1.1
     */
    public void playMusic() {
        m_Player.play();
        m_Player.setVolume(m_Volume);
    }

    /**
     * Stop the music.
     *
     * @see MediaPlayer#stop()
     * @since 1.1
     */
    public void stopMusic() {
        m_Player.stop();
    }

    /**
     * Return {@code true} if music is playing
     *
     * @return {@code true} if music is playing
     * @since 1.1
     */
    public boolean isPlayingMusic() {
        return m_Player.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Call {@code stopMusic} if music is stopping, otherwise call {@code playMusic()}.
     *
     * @see #isPlayingMusic()
     * @see #playMusic()
     * @see #stopMusic()
     * @since 1.1
     */
    public void toggleMusic() {
        if (isPlayingMusic()) {
            stopMusic();
        } else {
            playMusic();
        }
    }

    /**
     * Adjust the volume and if music do not play, call {@code playMusic()}.
     *
     * @param value Volume
     * @since 1.1
     */
    public void setVolume(Double value) {
        if(!isPlayingMusic()) {
            playMusic();
        }
        m_Volume = value;
        m_Player.setVolume(value);
    }
}