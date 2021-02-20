package sokoban.Model.level;

import java.util.Date;

/**
 *  <p>
 *      This class contains level name, start time, end time, user name and other game related information.
 *  </p>
 *
 * @author Wendi Han
 * @version 1.3
 * @see Date#getTime()
 */
public class J_LevelRecord {
    private final String m_LevelName;
    private Date m_StartTime;
    private Date m_EndTime;
    private int m_Move;
    private final String m_UserName;
    private long m_Time = 0;
    private String m_NewRecord = "No Record";
    private boolean m_IsComplete = false;

    /**
     * Initialize the level name, start time and user name.
     *
     * @param levelName - Level name
     * @param startTime - Start time
     * @param userName - End time
     * @since 1.1
     */
    public J_LevelRecord(String levelName, Date startTime, String userName) {
        m_LevelName = levelName;
        m_StartTime = startTime;
        m_Move = 0;
        m_UserName = userName;
    }

    /**
     * Initialize the level name, cost of time, count of move and user name.
     *
     * @param levelName - Level name
     * @param time - Time cost
     * @param move - Count of move
     * @param userName -User name
     * @since 1.2
     */
    public J_LevelRecord(String levelName, Long time, int move, String userName) {
        m_LevelName = levelName;
        m_Time = time;
        m_Move = move;
        m_UserName = userName;
    }

    /**
     * Initialize the level name, count of move and user name.
     *
     * @param levelName - Level name
     * @param move - Count of move
     * @param userName - User name
     * @since 1.3
     */
    public J_LevelRecord(String levelName, int move, String userName) {
        m_LevelName = levelName;
        m_Move = move;
        m_UserName = userName;
    }

    /**
     * Initialize the level name, cost of time and user name.
     *
     * @param levelName - Level name
     * @param time - Count of time
     * @param userName -User Name
     * @since 1.3
     */
    public J_LevelRecord(String levelName, long time, String userName) {
        m_LevelName = levelName;
        m_Time = time;
        m_UserName = userName;
    }

    /**
     * Get the time different between start time and end time.
     *
     * @return long - The time different between start time and end time.
     * @see Date#getTime()
     * @since 1.1
     */
    public long getTimeDiff() {
        long diff = m_EndTime.getTime() - m_StartTime.getTime();
        return diff;
    }

    /**
     * Contains level name, start name, end time and count of move.
     *
     * @return String - Contains level name, start name, end time and count of move
     * @since 1.1
     */
    @Override
    public String toString() {
        return "J_LevelRecord{" +
                "levelName='" + m_LevelName + '\'' +
                ", startTime=" + m_StartTime +
                ", endTime=" + m_EndTime +
                ", move=" + m_Move +
                '}';
    }


    /* Following is getter and setter*/


    /**
     * Set the start time of this level.
     *
     * @param startTime - Start time
     * @since 1.1
     */
    public void setStartTime(Date startTime) {
        m_StartTime = startTime;
    }

    /**
     * Set the end time of this level.
     *
     * @param endTime - End time
     * @since 1.1
     */
    public void setEndTime(Date endTime) {
        m_EndTime = endTime;
    }

    /**
     * Set the counts of move of this level.
     *
     * @param move - Counts of move
     * @since 1.1
     */
    public void setMove(int move) {
        m_Move = move;
    }

    /**
     * Get the name of level.
     *
     * @return {@code m_LevelName} - Level Name
     * @since 1.1
     */
    public String getLevelName() {
        return m_LevelName;
    }

    /**
     * Get the counts of move.
     *
     * @return {@code m_Move} - Counts of move
     * @since 1.1
     */
    public int getMove() {
        return m_Move;
    }

    /**
     * Get the start time.
     *
     * @return {@code m_StartTime} - Level start time
     * @since 1.1
     */
    public Date getStartTime() {
        return m_StartTime;
    }

    /**
     * Get the end time.
     *
     * @return {@code m_EndTime} - Level end time
     * @since 1.1
     */
    public Date getEndTime() {
        return m_EndTime;
    }

    /**
     * Get the user name.
     *
     * @return {@code m_UserName} - User name
     * @since 1.1
     */
    public String getUserName() {
        return m_UserName;
    }

    /**
     * Get the time different between start time and end time.
     *
     * @return {@code m_Time} - Time different between start time and end time
     * @since 1.1
     */
    public long getTime() {
        return m_Time;
    }

    /**
     * Set the record type of this level.
     *
     * @param newRecord - Type of record
     * @since 1.1
     */
    public void setNewRecord(String newRecord) {
        this.m_NewRecord = newRecord;
    }

    /**
     * Get the record type.
     *
     * @return {@code m_NewRecord} - Type of Record
     * @since 1.1
     */
    public String getNewRecord() {
        return m_NewRecord;
    }

    /**
     * Check if level has been completed.
     *
     * @return {@code true} if this level has been completed
     * @since 1.1
     */
    public boolean isLevelComplete() {
        return m_IsComplete;
    }

    /**
     * Set {@code m_IsComplete} to {@code true} if player completes this level.
     *
     * @param complete - Level completes or not
     * @since 1.1
     */
    public void setComplete(boolean complete) {
        m_IsComplete = complete;
    }

    /**
     * Set the time to past the level.
     *
     * @param time - Time to past level
     * @since 1.1
     */
    public void setM_Time(long time) {
        m_Time = time + m_Time;
    }

    /**
     * Reset the time to 0.
     *
     * @param time - Time to past level
     * @since 1.1
     */
    public void setTimeTo(long time) {
        m_Time = time;
    }
}
