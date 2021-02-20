package sokoban.Model.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Save the proposition list used by judging the truth pool.
 *
 * @author Wendi Han
 * @since 1.1
 */
public class J_PoolStatementSingleton {
    private List<String> tureProposition;
    private List<String> falseProposition;
    private volatile static J_PoolStatementSingleton instance;


    /**
     * Initialize the true and false proposition list.
     *
     * @since 1.1
     */
    private J_PoolStatementSingleton(){
        InputStream inputTrue = getClass().getClassLoader().getResourceAsStream("True.skb");
        InputStream inputFalse = getClass().getClassLoader().getResourceAsStream("False.skb");
        tureProposition = new ArrayList<>();
        falseProposition = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputTrue));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                tureProposition.add(line);
            }

            reader = new BufferedReader(new InputStreamReader(inputFalse));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                falseProposition.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    };

    /**
     * Initialize the {@code Media} using parameter if {@code instance} is not be initialized,
     * otherwise return {@code instance}.
     *
     * @return Media - Audio
     * @since 1.1
     */
    public static J_PoolStatementSingleton getInstance(){
        if(instance == null) {
            synchronized (J_PoolStatementSingleton.class) {
                if(instance == null) {
                    instance = new J_PoolStatementSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Get List of true proposition.
     *
     * @return List of true proposition.
     * @since 1.1
     */
    public List<String> getTureProposition() {
        return tureProposition;
    }

    /**
     * Get list of false proposition
     *
     * @return List of false Proposition
     * @since 1.1
     */
    public List<String> getFalseProposition() {
        return falseProposition;
    }
}
