package sokoban.GUI;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sokoban.Controller.J_GameController;
import sokoban.Model.game.J_GameEngine;
import sokoban.Model.game.J_ReloadGird;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.gameEngine.object.J_ImageSingleton;

import java.util.Date;

/**
 * Create the stage of game and show it to user.
 *
 * @author Wendi Han
 * @version 1.1
 * @see MenuItem
 * @see GridPane
 */
public class J_GameView extends Application {
    private Timeline m_Clock;
    private J_GameController gameController;
    private boolean m_ContinueLastGame = false;
    private GridPane m_Root;
    private GridPane m_GameGrid;
    private J_GameEngine m_GameEngine;
    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Initialize the game grid and get the game engine from first parameter (user is continuing the last game).
     *
     * @since 1.1
     * @param gameEngine  game engine
     */
    public J_GameView(J_GameEngine gameEngine) {
        m_Root = new GridPane();
        m_GameGrid = new GridPane();
        m_GameEngine = gameEngine;
    }

    /**
     * Create the item of stage shown to the user(menu, game state, game grid and perspective bar).
     *
     * @since 1.1
     * @see MenuItem
     * @see GridPane
     */
    @Override
    public void start(Stage stage){
        J_ImageSingleton imageSingleton = J_ImageSingleton.getInstance();

        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setAccelerator(KeyCombination.valueOf("SHIFT+ALT+L"));
        MenuItem menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setAccelerator(KeyCombination.valueOf("SHIFT+ALT+S"));
        MenuItem menuItemToMenu = new MenuItem("Back to menu");
        menuItemToMenu.setAccelerator(KeyCombination.valueOf("SHIFT+ALT+B"));
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setAccelerator(KeyCombination.valueOf("SHIFT+ALT+E"));
        MenuButton menuFile = new MenuButton("Game");
        menuFile.setFocusTraversable(false);
        menuFile.getItems().addAll(menuItemLoadGame,menuItemSaveGame,
                menuItemToMenu, menuItemExit);

        MenuItem menuItemUndo = new MenuItem("Undo");
        menuItemUndo.setAccelerator(KeyCombination.valueOf("SHIFT+U"));
        MenuItem menuItemNextLevel = new MenuItem("Next Level");
        menuItemNextLevel.setAccelerator(KeyCombination.valueOf("SHIFT+N"));
        MenuItem menuItemLastLevel = new MenuItem("Last Level");
        menuItemLastLevel.setAccelerator(KeyCombination.valueOf("SHIFT+L"));
        MenuItem radioMenuItemMusic = new MenuItem("Toggle Music");
        radioMenuItemMusic.setAccelerator(KeyCombination.valueOf("SHIFT+M"));
        Slider volume = new Slider(0, 1, 0);
        volume.setPrefWidth(100);
        CustomMenuItem volumeBar = new CustomMenuItem(volume);
        RadioMenuItem radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        radioMenuItemDebug.setAccelerator(KeyCombination.valueOf("SHIFT+ALT+D"));
        MenuItem menuItemResetLevel = new MenuItem("Reset Level");
        menuItemResetLevel.setAccelerator(KeyCombination.valueOf("SHIFT+R"));
        MenuButton menuLevel = new MenuButton("Level");
        menuLevel.setFocusTraversable(false);
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic, volumeBar
                ,radioMenuItemDebug, menuItemLastLevel
                , menuItemNextLevel, menuItemResetLevel);


        // set color
        MenuButton menuColor = new MenuButton("Color");
        menuColor.setFocusTraversable(false);

        ToggleGroup floor = new ToggleGroup();
        Menu menuFloorColor = new Menu("Floor Color");
        RadioMenuItem menuFloorYellow = new RadioMenuItem("Yellow"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("YELLOW")));
        RadioMenuItem menuFloorBrown = new RadioMenuItem("Brown"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("BROWN")));
        RadioMenuItem menuFloorGreen = new RadioMenuItem("Green"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("GREEN")));
        RadioMenuItem menuFloorGrey = new RadioMenuItem("Grey"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("GREY")));
        menuFloorGrey.setSelected(true);
        menuFloorBrown.setToggleGroup(floor);
        menuFloorYellow.setToggleGroup(floor);
        menuFloorGrey.setToggleGroup(floor);
        menuFloorGreen.setToggleGroup(floor);
        menuFloorColor.getItems().addAll(menuFloorYellow,menuFloorBrown,menuFloorGreen,menuFloorGrey);

        ToggleGroup crate = new ToggleGroup();
        Menu menuCrateColor = new Menu("Crate Color");
        RadioMenuItem menuCrateYellow = new RadioMenuItem("Yellow"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("YELLOW")));
        RadioMenuItem menuCrateBlack = new RadioMenuItem("Black"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("BLACK")));
        RadioMenuItem menuCrateBlue = new RadioMenuItem("Blue"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("BLUE")));
        RadioMenuItem menuCrateBrown = new RadioMenuItem("Brown"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("BROWN")));
        menuCrateBrown.setSelected(true);
        RadioMenuItem menuCrateKhaki = new RadioMenuItem("Khaki"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("KHAKI")));
        RadioMenuItem menuCratePurple = new RadioMenuItem("Purple"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("PURPLE")));
        RadioMenuItem menuCrateRed = new RadioMenuItem("Red"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("RED")));
        RadioMenuItem menuCrateGrey = new RadioMenuItem("Grey"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("GREY")));
        menuCrateBlack.setToggleGroup(crate);
        menuCrateBlue.setToggleGroup(crate);
        menuCrateBrown.setToggleGroup(crate);
        menuCrateGrey.setToggleGroup(crate);
        menuCrateYellow.setToggleGroup(crate);
        menuCrateKhaki.setToggleGroup(crate);
        menuCrateRed.setToggleGroup(crate);
        menuCratePurple.setToggleGroup(crate);
        menuCrateColor.getItems().addAll(menuCrateBlack,menuCrateBlue,menuCrateBrown,menuCrateKhaki,menuCratePurple
                ,menuCrateRed,menuCrateGrey,menuCrateYellow);

        ToggleGroup wall = new ToggleGroup();
        Menu menuWallColor = new Menu("Wall Color");
        RadioMenuItem menuWallBrown = new RadioMenuItem("Brown"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("BROWN")));
        RadioMenuItem menuWallBlack = new RadioMenuItem("Black"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("BLACK")));
        RadioMenuItem menuWallGrey = new RadioMenuItem("Grey"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("GREY")));
        menuWallGrey.setSelected(true);
        RadioMenuItem menuWallYellow = new RadioMenuItem("Yellow"
                , new ImageView(J_ImageSingleton.getInstance().getList().get("YELLOW")));
        menuWallBlack.setToggleGroup(wall);
        menuWallBrown.setToggleGroup(wall);
        menuWallGrey.setToggleGroup(wall);
        menuWallYellow.setToggleGroup(wall);
        menuWallColor.getItems().addAll(menuWallBrown,menuWallBlack,menuWallGrey,menuWallYellow);

        menuColor.getItems().addAll(menuCrateColor,menuFloorColor,menuWallColor);

        MenuItem menuItemGame = new MenuItem("About This Game");
        MenuButton menuAbout = new MenuButton("About");
        menuAbout.setFocusTraversable(false);
        menuAbout.getItems().addAll(menuItemGame);


        /********* game grid(subscene) ************/
        SubScene subScene = new SubScene(m_GameGrid, 1000, 680, true, SceneAntialiasing.BALANCED);
        m_GameGrid.setTranslateX(270);
        m_GameGrid.setTranslateY(140);
        PerspectiveCamera pc = new PerspectiveCamera();
        subScene.setCamera(pc);

        pc.setRotate(30);
        pc.setRotationAxis(new Point3D(1,0,0));
        /*************************************/


        /**********Change perspective (changeSlider)**************/
        HBox hbox = new HBox();
        hbox.setId("hbox");
        hbox.getStylesheets().add("button.css");

        MenuButton perspective = new MenuButton("Perspective");
        perspective.setFocusTraversable(false);

        ToggleGroup group = new ToggleGroup();
        RadioMenuItem degreeN30 = new RadioMenuItem("Degree -30");
        degreeN30.setToggleGroup(group);
        degreeN30.setUserData("-30");
        RadioMenuItem degreeN15 = new RadioMenuItem("Degree -15");
        degreeN15.setToggleGroup(group);
        degreeN15.setUserData("-15");
        RadioMenuItem degree0 = new RadioMenuItem("From top");
        degree0.setToggleGroup(group);
        degree0.setUserData("0");
        RadioMenuItem degree15 = new RadioMenuItem("Degree 15");
        degree15.setToggleGroup(group);
        degree15.setUserData("15");
        RadioMenuItem degree30 = new RadioMenuItem("Degree 30");
        degree30.setToggleGroup(group);
        degree30.setUserData("30");
        group.selectToggle(degree30);

        group.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
                    if (group.getSelectedToggle() != null) {
                        pc.setRotate(Integer.parseInt(group.getSelectedToggle().getUserData().toString()));
                        switch (Integer.parseInt(group.getSelectedToggle().getUserData().toString())) {
                            case 0, -15, -30 -> m_GameGrid.setTranslateY(110);
                            case 15 -> m_GameGrid.setTranslateY(120);
                            case 30 -> m_GameGrid.setTranslateY(140);
                        }
                    } });
        perspective.getItems().addAll(degreeN30, degreeN15, degree0, degree15, degree30);
        /************************/

        hbox.getChildren().addAll(menuFile, menuLevel, menuColor, perspective, menuAbout);
        hbox.setTranslateY(20);

        /***********  game state (sp)  *************/
        AnchorPane ap = new AnchorPane();
        Label gameInfoName = new Label("start");
        gameInfoName.setFont(new Font("Consolas", 15));
        gameInfoName.setTranslateX(-135);
        gameInfoName.setStyle("-fx-text-fill: #FFFFFF;");
        Label gameInfoMove = new Label("start");
        gameInfoMove.setFont(new Font("Consolas", 15));
        gameInfoMove.setTranslateX(10);
        gameInfoMove.setStyle("-fx-text-fill: #FFFFFF;");
        Label gameInfoTime = new Label("start");
        gameInfoTime.setFont(new Font("Consolas", 15));
        gameInfoTime.setTranslateX(140);
        gameInfoTime.setStyle("-fx-text-fill: #FFFFFF;");
        ImageView iv = new ImageView(imageSingleton.getList().get("TABLE"));

        StackPane sp = new StackPane();
        sp.getChildren().addAll(iv, gameInfoName, gameInfoMove, gameInfoTime);
        sp.setTranslateX(320);
        sp.setTranslateY(40);
        /**************************************/


        ap.getChildren().addAll(subScene,sp);
        ap.autosize();
        ap.setTranslateX(-50);

        Background bg = new Background(new BackgroundImage(imageSingleton.getList().get("gameBG")
                , null, null, null, null));
        m_Root.setBackground(bg);
        m_Root.add(hbox, 0,0);
        m_Root.add(ap, 0, 1);

        gameController = new J_GameController(stage, this);

        m_GameEngine = gameController.getGameEngine();

        m_Clock = new Timeline(
                new KeyFrame(Duration.seconds(0), evt -> {
                    if (m_GameEngine.isGameComplete()) {
                        m_Clock.stop();
                    } else {
                        gameInfoName.setText( m_GameEngine.getCurrentLevel().getName());
                        gameInfoMove.setText(String.valueOf(m_GameEngine.getMove()
                                .getMovesCount().get(m_GameEngine.getCurrentLevel().getName())));
                        gameInfoTime.setText((m_GameEngine.getLr().getTime()/1000 + ((new Date()).getTime()
                                - m_GameEngine.getLr().getStartTime().getTime()) / 1000) + "s");
                    }
                }), new KeyFrame(Duration.seconds(0.5))
        );
        m_Clock.setCycleCount(Animation.INDEFINITE);
        m_Clock.play();

        menuItemLoadGame.setOnAction(actionEvent -> gameController.loadGame());
        menuItemSaveGame.setOnAction(actionEvent -> gameController.saveGame());
        menuItemToMenu.setOnAction(actionEvent -> gameController.backToMenu());
        menuItemExit.setOnAction(actionEvent -> gameController.closeGame());
        menuItemUndo.setOnAction(actionEvent -> gameController.undo());
        menuItemNextLevel.setOnAction(actionEvent -> gameController.nextLevel());
        menuItemLastLevel.setOnAction(actionEvent -> gameController.lastLevel());
        radioMenuItemMusic.setOnAction(actionEvent -> gameController.toggleMusic());
        volumeBar.setOnAction(actionEvent -> gameController.setVolume(volume.getValue()));
        radioMenuItemDebug.setOnAction(actionEvent -> gameController.toggleDebug());
        menuItemResetLevel.setOnAction(actionEvent -> gameController.resetLevel());
        menuFloorYellow.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.FLOOR, "YELLOW"));
        menuFloorBrown.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.FLOOR, "BROWN"));
        menuFloorGreen.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.FLOOR, "GREEN"));
        menuFloorGrey.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.FLOOR, "GREY"));
        menuCrateYellow.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.CRATE, "YELLOW"));
        menuCrateBlack.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.CRATE, "BLACK"));
        menuCrateBlue.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.CRATE, "BLUE"));
        menuCrateBrown.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.CRATE, "BROWN"));
        menuCrateKhaki.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.CRATE, "KHAKI"));
        menuCratePurple.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.CRATE, "PURPLE"));
        menuCrateRed.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.CRATE, "RED"));
        menuCrateGrey.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.CRATE, "GREY"));
        menuWallYellow.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.WALL, "YELLOW"));
        menuWallGrey.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.WALL, "GREY"));
        menuWallBlack.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.WALL, "BLACK"));
        menuWallBrown.setOnAction(actionEvent -> gameController.toggleColor(E_GameObject.WALL, "BROWN"));
        menuItemGame.setOnAction(actionEvent -> gameController.showAbout());


        stage.setOnCloseRequest(windowEvent -> gameController.closeGame());


        stage.setTitle(m_GameEngine.getGameName());
        stage.setScene(new Scene(m_Root));
        stage.sizeToScene();
        if(stage.getStyle() != StageStyle.UNDECORATED) {
            stage.initStyle(StageStyle.UNDECORATED);
        }
        m_Root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        m_Root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.show();
        stage.setMaxHeight(stage.getHeight());
        stage.setMaxWidth(stage.getWidth());
        J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, stage);
    }

    /**
     * Get the controller of game.
     *
     * @since 1.1
     * @return J_GameController
     */
    public J_GameController getGameController() {
        return gameController;
    }

    /**
     * Return {@code true} if player is continuing the last game
     *
     * @return {@code true} if player is continuing the last game
     * @since 1.1
     */
    public boolean isM_ContinueLastGame() {
        return m_ContinueLastGame;
    }

    /**
     * Return Grid of game
     *
     * @return Grid of game
     * @since 1.1
     */
    public GridPane getM_GameGrid() {
        return m_GameGrid;
    }

    /**
     * Return game engine of current game.
     *
     * @return Game engine of current game
     * @since 1.1
     */
    public J_GameEngine getM_GameEngine() {
        return m_GameEngine;
    }

    /**
     * Setter method of variable 'continueLastGame'
     *
     * @param continueLastGame  Check if user is continuing the last game.
     * @since 1.4
     */
    public void setContinueLastGame (Boolean continueLastGame){
        m_ContinueLastGame = continueLastGame;
    }

    /**
     * Set the game engine.
     *
     * @param m_GameEngine Game engine
     * @since 1.2
     */
    public void setM_GameEngine(J_GameEngine m_GameEngine) {
        this.m_GameEngine = m_GameEngine;
    }
}
