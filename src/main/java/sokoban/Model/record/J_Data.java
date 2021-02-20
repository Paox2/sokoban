package sokoban.Model.record;

import sokoban.Model.level.J_LevelRecord;

import java.io.*;
import java.util.LinkedHashMap;

/**
 *  <p>
 *      Read and write the highest record.<br>
 *      Method {@code saveHighScore(String levelName, LinkedHashMap<String, Long> higherLevel, String userName)}
 *      save the new score based on {@code levelName}.<br>
 *      Method {@code loadHighScore()} load the highest record.<br>
 *      Method {@code judgeNewRecordSpecialLevel(J_LevelRecord lr)} judge if new record is created for certain level.<br>
 *      Method {@code loadLevelHighestRecord(String name)} load the highest record of certain level.
 *  </p>
 * @author Wendi Han
 * @version 1.2
 * @see LinkedHashMap
 * @see StringBuilder
 * @see FileOutputStream
 * @see BufferedWriter
 * @see FileInputStream
 * @see InputStreamReader
 * @see BufferedReader
 * @see J_LevelRecord
 */
public class J_Data {

    /**
     * Save the highest record of certain level based on {@code levelName}. The key of {@code higherLevel}
     * is the type of new record, and value is the time or count of moves.
     *
     * @see LinkedHashMap
     * @see StringBuilder
     * @see FileOutputStream
     * @see BufferedWriter
     * @since 1.2
     */
    public void saveHighScore(){
        try
        {
            StringBuilder content = new StringBuilder();
            LinkedHashMap<String, J_LevelRecord> timeRecord = J_HighestDataSingleton.getInstance().getTimeRecord();
            LinkedHashMap<String, J_LevelRecord> moveRecord = J_HighestDataSingleton.getInstance().getMoveRecord();
            content.append("***Time : \n");
            for (J_LevelRecord lr : timeRecord.values()) {
                content.append("Record:").append(lr.getUserName())
                        .append(":").append(lr.getLevelName())
                        .append(":").append(lr.getTime()).append("\n");;
            }

            content.append("***Moves : \n");
            for (J_LevelRecord lr : moveRecord.values()) {
                content.append("Record:").append(lr.getUserName())
                        .append(":").append(lr.getLevelName())
                        .append(":").append(lr.getMove()).append("\n");

            }

            FileOutputStream fos = new FileOutputStream("src/main/resources/RECORD/high_score.skb");
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(content.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the highest score of each level if file exists and is not empty, otherwise return null.
     *
     * @return listScore if file exists and is not empty.
     * @see FileInputStream
     * @see InputStreamReader
     * @see BufferedReader
     * @see StringBuilder
     * @see J_LevelRecord
     * @since 1.1
     */
    public LinkedHashMap<String, LinkedHashMap<String, J_LevelRecord>> loadHighScore() {
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/RECORD/high_score.skb");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            LinkedHashMap<String, LinkedHashMap<String, J_LevelRecord>> listScore = new LinkedHashMap<>();
            String content;
            LinkedHashMap<String, J_LevelRecord> timeRecord = new LinkedHashMap<>();
            LinkedHashMap<String, J_LevelRecord> moveRecord = new LinkedHashMap<>();
            boolean isTime = false;

            while ((content = br.readLine()) != null) {
                if (content.contains("***Time")) {
                    isTime = true;
                } else if (content.contains("***Moves")) {
                    isTime = false;
                }
                if (content.contains("Record")) {
                    String[] contentSplite = content.split(":");
                    if(contentSplite[3] == null)    continue;
                    J_LevelRecord jl;
                    if (isTime) {
                        jl = new J_LevelRecord(contentSplite[2], Long.parseLong(contentSplite[3]), contentSplite[1]);
                        timeRecord.put(contentSplite[2], jl);
                    } else {
                        jl = new J_LevelRecord(contentSplite[2], Integer.parseInt(contentSplite[3]), contentSplite[1]);
                        moveRecord.put(contentSplite[2], jl);
                    }
                }
            }

            listScore.put("Time Record", timeRecord);
            listScore.put("Moves Record", moveRecord);
            br.close();
            return listScore;
        } catch (IOException e) {
            return null;
        }
    }
}
