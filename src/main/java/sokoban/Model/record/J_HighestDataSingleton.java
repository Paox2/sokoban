package sokoban.Model.record;

import sokoban.Model.level.J_LevelRecord;

import java.util.LinkedHashMap;

/**
 *  <p>
 *      Save the highest score, ready to update and write into the file.
 *  </p>
 *
 * @author Wendi Han
 * @version 1.2
 */
public class J_HighestDataSingleton {
    private LinkedHashMap<String, J_LevelRecord> m_TimeRecord;
    private LinkedHashMap<String, J_LevelRecord> m_MoveRecord;
    private final J_Data data;
    private volatile static J_HighestDataSingleton instance;

    /**
     * Initialize the time record list and move record list. If file is empty then just give a new instance.
     *
     * @since 1.1
     */
    private J_HighestDataSingleton() {
        data = new J_Data();
        LinkedHashMap<String, LinkedHashMap<String, J_LevelRecord>>levelRecord = data.loadHighScore();
        if(levelRecord != null) {
            m_TimeRecord = levelRecord.get("Time Record");
            m_MoveRecord = levelRecord.get("Moves Record");
        } else {
            m_TimeRecord = new LinkedHashMap<>();
            m_MoveRecord = new LinkedHashMap<>();
        }
    }


    /**
     * Get the instance of class which saves the highest score.
     *
     * @return class contains highest score list
     * @since 1.2
     */
    public static J_HighestDataSingleton getInstance(){
        if(instance == null) {
            synchronized (J_HighestDataSingleton.class) {
                if(instance == null) {
                    instance = new J_HighestDataSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Get the time record list.
     *
     * @return time record list
     * @since 1.1
     */
    public LinkedHashMap<String, J_LevelRecord> getTimeRecord() {
        return m_TimeRecord;
    }

    /**
     * Get the move record list.
     *
     * @return move record list
     * @since 1.1
     */
    public LinkedHashMap<String, J_LevelRecord> getMoveRecord() {
        return m_MoveRecord;
    }

    /**
     * Update the time record list.
     *
     * @param levelName  Level name
     * @param lr  Record of certain level
     * @since 1.1
     */
    public void setNewTimeRecord(String levelName, J_LevelRecord lr) {
        J_LevelRecord record = new J_LevelRecord(levelName, lr.getTime(), lr.getUserName());
        m_TimeRecord.put(levelName, record);
        data.saveHighScore();
    }

    /**
     * Update the move record list.
     *
     * @param levelName Level name
     * @param lr Record of certain level
     * @since 1.1
     */
    public void setNewMoveRecord(String levelName, J_LevelRecord lr) {
        m_MoveRecord.put(levelName, lr);
        data.saveHighScore();
    }

    /**
     * Return 0 if no new record is created, 1 represent new time record
     * 2 represent new move record and 3 represent new time and move record
     *
     * @param lr - The record of certain level
     * @return Integer - See the explain of method.
     * @see J_LevelRecord
     * @since 1.2
     */
    public Integer judgeNewRecordSpecialLevel(J_LevelRecord lr) {
        Integer m_NoRecord = 0;
        int recordType = m_NoRecord;
        LinkedHashMap<String, Long> score = loadLevelHighestRecord(lr.getLevelName());
        Integer m_TwoRecord = 3;
        if (score != null && score.containsKey("time")) {
            if (score.get("time") > lr.getTime()) {
                if (score.containsKey("move") && score.get("move") > lr.getMove()) {
                    recordType = m_TwoRecord;
                } else {
                    Integer m_TimeRecords = 1;
                    recordType = m_TimeRecords;
                }
            } else if (score.containsKey("move") && score.get("move") > lr.getMove()) {
                Integer m_MovesRecords = 2;
                recordType = m_MovesRecords;
            }
        } else {
            recordType = m_TwoRecord;
        }
        return recordType;
    }

    /**
     * Load highest level record of certain level. Use {@code String} in {@code LinkedHashMap<String, Long>}
     * to save the record type.
     *
     * @param name - Level name
     * @return LinkedHashMap
     * @see LinkedHashMap
     * @since 1.2
     */
    public LinkedHashMap<String, Long> loadLevelHighestRecord(String name) {
        LinkedHashMap<String, Long> record = new LinkedHashMap<>();
        if(m_TimeRecord.containsKey(name)) {
            record.put("time", m_TimeRecord.get(name).getTime());
            record.put("move", (long) m_MoveRecord.get(name).getMove());
            return record;
        } else {
            return null;
        }
    }
}
