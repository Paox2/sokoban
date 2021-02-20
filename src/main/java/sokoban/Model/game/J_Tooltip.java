package sokoban.Model.game;

import javafx.animation.PauseTransition;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Extends the class tooltip in Javafx to show some informs.
 *
 * @author Wendi Han
 * @version 1.1
 * @see Tooltip
 * @see Duration
 * @see PauseTransition
 */
public class J_Tooltip extends Tooltip {
    private Tooltip tooltip = new Tooltip();
    private Stage stage;

    /**
     * Set the position and text font of the inform.
     *
     * @param text  Inform message
     * @param stage  Inform stage
     * @since 1.1
     */
    public J_Tooltip(String text, Stage stage) {
        tooltip.setFont(new Font(14));
        tooltip.setText(text);
        tooltip.setX(stage.getX() + 400);
        tooltip.setY(stage.getY() + 100);
        this.stage = stage;
    }

    /**
     * Show the inform message.
     *
     * @see Tooltip#show()
     * @param duration  Time duration shows to the user
     * @since 1.1
     * @see Duration
     * @see PauseTransition
     */
    public void show(Duration duration) {
        tooltip.show(stage);
        PauseTransition fadeTransition = new PauseTransition(duration);
        fadeTransition.setOnFinished(e -> tooltip.hide());
        fadeTransition.playFromStart();
    }
}
