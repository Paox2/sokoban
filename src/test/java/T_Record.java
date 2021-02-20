import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sokoban.Model.level.J_LevelRecord;
import sokoban.Model.record.J_HighestDataSingleton;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Wendi Han
 */
public class T_Record {
    J_HighestDataSingleton highestSave;
    LinkedHashMap<String, J_LevelRecord> timeRecord;
    LinkedHashMap<String, J_LevelRecord> moveRecord;

    @BeforeEach
    void setup() {
        highestSave = J_HighestDataSingleton.getInstance();
        timeRecord = highestSave.getTimeRecord();
        moveRecord = highestSave.getMoveRecord();
    }

    @Test
    void testRecordSuccessfullyLoad() {
        timeRecord.size();
        moveRecord.size();
        assertTrue(true);
    }

    @Test
    void testUpdateTimeRecord() {
        highestSave.setNewTimeRecord("Level1"
                , new J_LevelRecord("Level1", (long) 10, "John"));
        highestSave = null;
        highestSave = J_HighestDataSingleton.getInstance();
        timeRecord = highestSave.getTimeRecord();
        assertEquals("John", timeRecord.get("Level1").getUserName());
        assertEquals(10, timeRecord.get("Level1").getTime());
    }

    @Test
    void testUpdateMoveRecord() {
        highestSave.setNewMoveRecord("Level1"
                , new J_LevelRecord("Level1", 10, "John"));
        highestSave = null;
        highestSave = J_HighestDataSingleton.getInstance();
        moveRecord = highestSave.getMoveRecord();
        assertEquals("John", moveRecord.get("Level1").getUserName());
        assertEquals(10, moveRecord.get("Level1").getMove());
    }

    // Finish this part need to delete the "high_score.skb" in "src/sokoban.main/resources/RECORD"
    @Test
    void testWhenFileNotExist() {
        timeRecord.size();
        moveRecord.size();
        assertTrue(true);
    }

    @Test
    void testLevelNotExist() {
        highestSave.setNewMoveRecord("Level2"
                , new J_LevelRecord("Level2", 10, "John"));
        highestSave = null;
        highestSave = J_HighestDataSingleton.getInstance();
        moveRecord = highestSave.getMoveRecord();
        assertEquals("John", moveRecord.get("Level2").getUserName());
        assertEquals(10, moveRecord.get("Level2").getMove());
    }
}
