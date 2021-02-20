package sokoban.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Main method load the fxml file about the scene of menun
 *
 * @author Wendi Han
 * @version 1.1
 */
public class Main extends Application {

    /**
     * Start the game
     *
     * @param args Parameter
     * @since 1.1
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Start the main view.
     *
     * @param stage Stage
     * @throws Exception May appear when
     * @since 1.1
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent roots;
        roots = FXMLLoader.load(new URL("file:src/main/java/sokoban/GUI/Menu.fxml"));
        stage.setTitle("Sokoban");
        stage.setScene(new Scene(roots, 943, 448));
        stage.sizeToScene();
        stage.show();
        stage.setMaxHeight(stage.getHeight());
        stage.setMaxWidth(stage.getWidth());
    }
}
