package sokoban.Model.ImageAnimation;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import sokoban.Model.gameEngine.object.J_ImageSingleton;

import java.util.ArrayList;

/**
 * Create image animation. Animation is implemented by 3D (translation in z) and form in 3 picture
 *
 * @author Wendi Han
 * @version 1.1
 */
public class J_ImageAnimation {
    private double m_Img_depth = 0;
    private double m_Img1_x = 0;
    private double m_Img2_x = 0;
    private double m_Img3_x = 0;
    private ArrayList<ImageView> m_ImageList;

    /**
     * Get the pane of animation.
     *
     * @author Wendi Han
     * @since 1.1
     * @return Pane
     * @see SubScene
     * @see PerspectiveCamera
     * @see ImageView
     */
    public Pane getSlideShow() {
        int width = 450;
        int height = 260;
        m_Img_depth = -50;
        ImageView button_l = new ImageView();
        button_l.setImage(J_ImageSingleton.getInstance().makeTransparent(new Image("LEFT.png")));
        button_l.setPreserveRatio(true);
        button_l.setFitWidth(25);
        button_l.setFitHeight(25);
        ImageView button_r = new ImageView();
        button_r.setImage(J_ImageSingleton.getInstance().makeTransparent(new Image("RIGHT.png")));
        button_r.setPreserveRatio(true);
        button_r.setFitWidth(25);
        button_r.setFitHeight(25);

        HBox hbox = new HBox(width - button_l.prefWidth(-1));
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(button_l, button_r);

        ImageView img1 = new ImageView(J_ImageSingleton.getInstance().
                makeTransparent(new Image("BG1.png")));
        img1.setFitWidth(width / 1.5);
        img1.setFitHeight(height / 1.5);
        ImageView img2 = new ImageView(J_ImageSingleton.getInstance().
                makeTransparent(new Image("BG2.png")));
        img2.setFitWidth(width / 1.5);
        img2.setFitHeight(height / 1.5);
        ImageView img3 = new ImageView(J_ImageSingleton.getInstance().
                makeTransparent(new Image("BG3.png")));
        img3.setFitWidth(width / 1.5);
        img3.setFitHeight(height / 1.5);

        m_Img1_x = 0 + m_Img_depth;
        m_Img2_x = width / 2 - img1.getFitWidth() / 2;
        m_Img3_x = width - img3.getFitWidth() - m_Img_depth;
        img1.setTranslateX(m_Img1_x);
        img2.setTranslateX(m_Img2_x);
        img3.setTranslateX(m_Img3_x);

        double h = height / 2 - img1.prefHeight(-1) / 2;
        img1.setTranslateY(h);
        img2.setTranslateY(h);
        img3.setTranslateY(h);

        img1.setTranslateZ(0);
        img2.setTranslateZ(m_Img_depth);
        img3.setTranslateZ(0);

        m_ImageList = new ArrayList<>();
        m_ImageList.add(img1);
        m_ImageList.add(img2);
        m_ImageList.add(img3);

        button_l.setOnMouseClicked(mouseEvent -> rightToLeft());

        button_r.setOnMouseClicked(mouseEvent -> leftToRight());

        AnchorPane ap = new AnchorPane();
        ap.getChildren().addAll(img1, img2, img3);

        SubScene subScene = new SubScene(ap, width, height, true, SceneAntialiasing.BALANCED);
        PerspectiveCamera pc = new PerspectiveCamera();
        subScene.setCamera(pc);

        StackPane pane = new StackPane();
        ap.setStyle("-fx-background-color : transparent");
        pane.setStyle("-fx-background-color :  transparent");
        pane.getChildren().addAll(subScene,hbox);

        return pane;
    }

    /**
     * Response to the left button.
     *
     * @author Wendi Han
     * @since 1.1
     * @see J_ImageAnimation#leftToMiddleAnimation(ImageView)
     * @see J_ImageAnimation#middleToRightAnimation(ImageView)
     * @see J_ImageAnimation#rightToLeftAnimation(ImageView)
     */
    public void rightToLeft() {
        ImageView img1 = m_ImageList.get(0);
        ImageView img2 = m_ImageList.get(1);
        ImageView img3 = m_ImageList.get(2);
        leftToMiddleAnimation(img1);
        middleToRightAnimation(img2);
        rightToLeftAnimation(img3);

        m_ImageList.clear();
        m_ImageList.add(img3);
        m_ImageList.add(img1);
        m_ImageList.add(img2);
    }

    /**
     * Response to the right button.
     *
     * @author Wendi Han
     * @since 1.1
     * @see J_ImageAnimation#rightToMiddleAnimation(ImageView)
     * @see J_ImageAnimation#middleToLeftAnimation(ImageView)
     * @see J_ImageAnimation#leftToRightAnimation(ImageView)
     */
    public void leftToRight() {
        ImageView img1 = m_ImageList.get(0);
        ImageView img2 = m_ImageList.get(1);
        ImageView img3 = m_ImageList.get(2);

        rightToMiddleAnimation(img3);
        middleToLeftAnimation(img2);
        leftToRightAnimation(img1);
        m_ImageList.clear();
        m_ImageList.add(img2);
        m_ImageList.add(img3);
        m_ImageList.add(img1);
    }

    /**
     * Move the left image to middle.
     *
     * @author Wendi Han
     * @param iv  Image
     * @since 1.1
     */
    public void leftToMiddleAnimation(ImageView iv) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), iv);
        tt.setFromX(m_Img1_x);
        tt.setFromZ(0);
        tt.setToX(m_Img2_x);
        tt.setToZ(m_Img_depth);
        tt.play();
    }

    /**
     * Move the midddle image to right.
     *
     * @author Wendi Han
     * @param iv  Image
     * @since 1.1
     */
    public void middleToRightAnimation(ImageView iv) {

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5));
        tt.setFromX(m_Img2_x);
        tt.setFromZ(m_Img_depth);
        tt.setToX(m_Img3_x);
        tt.setToZ(0);

        FadeTransition ft1 = new FadeTransition(Duration.seconds(0.25));
        ft1.setFromValue(1);
        ft1.setToValue(0.8);

        FadeTransition ft2 = new FadeTransition(Duration.seconds(0.25));
        ft2.setFromValue(0.8);
        ft2.setToValue(1);

        SequentialTransition st = new SequentialTransition();
        st.getChildren().addAll(ft1, ft2);

        ParallelTransition pt = new ParallelTransition();
        pt.setNode(iv);
        pt.getChildren().addAll(tt, st);
        pt.play();
    }

    /**
     * Move the right image to left.
     *
     * @author Wendi Han
     * @param iv  Image
     * @since 1.1
     */
    public void rightToLeftAnimation(ImageView iv) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), iv);
        tt.setFromX(m_Img3_x);
        tt.setToX(m_Img1_x);
        tt.play();
    }

    /**
     * Move the right image to middle.
     *
     * @author Wendi Han
     * @param iv  Image
     * @since 1.1
     */
    public void rightToMiddleAnimation(ImageView iv) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), iv);
        tt.setFromX(m_Img3_x);
        tt.setFromZ(0);
        tt.setToX(m_Img2_x);
        tt.setToZ(m_Img_depth);
        tt.play();
    }

    /**
     * Move the middle image to left.
     *
     * @author Wendi Han
     * @param iv  Image
     * @since 1.1
     */
    public void middleToLeftAnimation(ImageView iv) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5));
        tt.setFromX(m_Img2_x);
        tt.setFromZ(m_Img_depth);
        tt.setToX(m_Img1_x);
        tt.setToZ(0);

        FadeTransition ft1 = new FadeTransition(Duration.seconds(0.25));
        ft1.setFromValue(1);
        ft1.setToValue(0.8);

        FadeTransition ft2 = new FadeTransition(Duration.seconds(0.25));
        ft2.setFromValue(0.8);
        ft2.setToValue(1);

        SequentialTransition st = new SequentialTransition();
        st.getChildren().addAll(ft1, ft2);

        ParallelTransition pt = new ParallelTransition();
        pt.setNode(iv);
        pt.getChildren().addAll(tt, st);

        pt.play();
    }

    /**
     * Move the left image to right.
     *
     * @author Wendi Han
     * @param iv  Image
     * @since 1.1
     */
    public void leftToRightAnimation(ImageView iv) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), iv);
        tt.setFromX(m_Img1_x);
        tt.setToX(m_Img3_x);
        tt.play();
    }
}
