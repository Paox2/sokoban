package sokoban.Model.gameEngine.object;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.HashMap;

/**
 * This class saves all the picture in a hashmap and avoid repeatedly access the image file decreased efficiency.
 * @author Wendi Han
 * @version 1.2
 * @see javafx.scene.image.ImageView
 * @see HashMap
 * @see PixelReader
 * @see PixelWriter
 */
public class J_ImageSingleton {
    private HashMap<String, Image> imageList = new HashMap<>();
    private volatile static J_ImageSingleton instance;

    /**
     * Add all the images to the hash map.
     *
     * @since 1.1
     */
    private J_ImageSingleton() {
        imageList.put("CRATE_BLACK", new Image("CRATE_BLACK.png"));
        imageList.put("CRATE_BLUE", new Image("CRATE_BLUE.png"));
        imageList.put("CRATE_BROWN", new Image("CRATE_BROWN.png"));
        imageList.put("CRATE_KHAKI", new Image("CRATE_KHAKI.png"));
        imageList.put("CRATE_PURPLE", new Image("CRATE_PURPLE.png"));
        imageList.put("CRATE_RED", new Image("CRATE_RED.png"));
        imageList.put("CRATE_GREY", new Image("CRATE_GREY.png"));
        imageList.put("CRATE_YELLOW", new Image("CRATE_YELLOW.png"));
        imageList.put("CRATE_ON_DIAMOND_BLACK", new Image("CRATE_ON_DIAMOND_BLACK.png"));
        imageList.put("CRATE_ON_DIAMOND_BLUE", new Image("CRATE_ON_DIAMOND_BLUE.png"));
        imageList.put("CRATE_ON_DIAMOND_BROWN", new Image("CRATE_ON_DIAMOND_BROWN.png"));
        imageList.put("CRATE_ON_DIAMOND_KHAKI", new Image("CRATE_ON_DIAMOND_KHAKI.png"));
        imageList.put("CRATE_ON_DIAMOND_PURPLE", new Image("CRATE_ON_DIAMOND_PURPLE.png"));
        imageList.put("CRATE_ON_DIAMOND_RED", new Image("CRATE_ON_DIAMOND_RED.png"));
        imageList.put("CRATE_ON_DIAMOND_GREY", new Image("CRATE_ON_DIAMOND_GREY.png"));
        imageList.put("CRATE_ON_DIAMOND_YELLOW", new Image("CRATE_ON_DIAMOND_YELLOW.png"));
        imageList.put("DIAMOND_BROWN", makeTransparent(new Image("DIAMOND_BROWN.png")));
        imageList.put("DIAMOND_GREEN", makeTransparent(new Image("DIAMOND_GREEN.png")));
        imageList.put("DIAMOND_GREY", makeTransparent(new Image("DIAMOND_GREY.png")));
        imageList.put("DIAMOND_YELLOW", makeTransparent(new Image("DIAMOND_YELLOW.png")));
        imageList.put("FLOOR_BROWN", new Image("FLOOR_BROWN.png"));
        imageList.put("FLOOR_GREEN", new Image("FLOOR_GREEN.png"));
        imageList.put("FLOOR_GREY", new Image("FLOOR_GREY.png"));
        imageList.put("FLOOR_YELLOW", new Image("FLOOR_YELLOW.png"));
        imageList.put("KEEPER_D", new Image("KEEPER_D.png"));
        imageList.put("KEEPER_U", new Image("KEEPER_U.png"));

        imageList.put("KEEPER_R", new Image("KEEPER_R.png"));
        imageList.put("KEEPER_L", new Image("KEEPER_L.png"));
        imageList.put("WALL_BROWN", new Image("WALL_BROWN.png"));
        imageList.put("WALL_BLACK", new Image("WALL_BLACK.png"));
        imageList.put("WALL_GREY", new Image("WALL_GREY.png"));
        imageList.put("WALL_YELLOW", new Image("WALL_YELLOW.png"));

        imageList.put("BLACK", makeTransparent(new Image("BLACK.png", 15, 15,false, false)));
        imageList.put("RED",makeTransparent(new Image("RED.png", 15, 15,false, false)));
        imageList.put("YELLOW", makeTransparent(new Image("YELLOW.png", 15, 15,false, false)));
        imageList.put("GREEN", makeTransparent(new Image("GREEN.png", 15, 15,false, false)));
        imageList.put("GREY", makeTransparent(new Image("GREY.png", 15, 15,false, false)));
        imageList.put("KHAKI", makeTransparent(new Image("KHAKI.png", 15, 15,false, false)));
        imageList.put("PURPLE", makeTransparent(new Image("PURPLE.png", 15, 15,false, false)));
        imageList.put("BLUE", makeTransparent(new Image("BLUE.png", 15, 15,false, false)));
        imageList.put("BROWN", makeTransparent(new Image("BROWN.png", 15, 15,false, false)));

        imageList.put("GHOST", new Image("GHOST.png", 15, 15, false, false));
        imageList.put("PIPELINE", makeTransparent(new Image("PIPELINE.png",15,15,false,false)));
        imageList.put("SHADOW", makeTransparent(new Image("SHADOW.png",25,25,false,false)));
        imageList.put("POOL", new Image("POOL.png", 15, 15, false, false));

        imageList.put("Level1", makeTransparent(new Image("Level1.png", 329, 233, false, false)));
        imageList.put("Level2", makeTransparent(new Image("Level2.png", 329, 233, false, false)));
        imageList.put("Level3", makeTransparent(new Image("Level3.png", 329, 233, false, false)));
        imageList.put("Level4", makeTransparent(new Image("Level4.png", 329, 233, false, false)));

        imageList.put("TABLE", makeTransparent(new Image("TABLE.png", 400, 35, false,false)));
        imageList.put("gameBG", new Image("gameBG.png", 1050, 760, false, false));
    }

    /**
     * Initialize the {@code imageList} using parameter if {@code instance} is not be initialized,
     * otherwise return {@code instance}.
     *
     * @return Media - Audio
     */
    public static J_ImageSingleton getInstance(){
        if(instance == null) {
            synchronized (J_ImageSingleton.class) {
                if(instance == null) {
                    instance = new J_ImageSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Return a lists contained all the image with their name.
     *
     * @return {@code imageList} - A list of images
     * @since 1.1
     */
    public HashMap<String, Image> getList(){
        return imageList;
    }

    /**
     * Make the image transparent.
     *
     * @param inputImage - A image
     * @return Image after making transparent
     * @since 1.1
     */
    public Image makeTransparent(Image inputImage) {
        int W = (int) inputImage.getWidth();
        int H = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb = reader.getArgb(x, y);

                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                if ((r >= 0xF0 && g >= 0xF0 && b >= 0xF0)) {
                    argb &= 0x00FFFFFF;
                }

                writer.setArgb(x, y, argb);
            }
        }

        return outputImage;
    }
}