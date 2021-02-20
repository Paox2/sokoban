module sokoban {
    requires java.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires java.desktop;
    requires java.logging;
    opens sokoban.GUI;
    opens sokoban.Model.game;
    opens sokoban.Model.gameEngine.object.ghost;
    opens sokoban.Model.gameEngine.object;
    opens sokoban.Model.gameEngine.music;
    opens sokoban.Model.gameEngine.move;
    opens sokoban.Model.gameEngine.debug;
    opens sokoban.Model.ImageAnimation;
    opens sokoban.Model.message;
    opens sokoban.Model.log;
    opens sokoban.Model.record;
    opens sokoban.Model.level;
    opens sokoban.Controller;
    opens sokoban.main;
}