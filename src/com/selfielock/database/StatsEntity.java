package com.selfielock.database;

public class StatsEntity {
    
    private int numberOfWin;
    private int numberOfFail;
    private int timesPlayed;
    private int achievementsUnlocked;
    private String userEmail;
    
    public StatsEntity(int numberOfWin, int numberOfFail, int timesPlayed, int achievementsUnlocked, String userEmail)
    {
        this.numberOfWin = numberOfWin;
        this.numberOfFail = numberOfFail;
        this.timesPlayed = timesPlayed;
        this.achievementsUnlocked = achievementsUnlocked;
        this.userEmail = userEmail;
    }
    
    public StatsEntity() 
    {
        this.numberOfWin = 0;
        this.numberOfFail = 0;
        this.timesPlayed = 0;
        this.achievementsUnlocked = 0;
        this.userEmail = "";
    }

    public int getNumberOfWin() {
        return numberOfWin;
    }

    public void setNumberOfWin(int numberOfWin) {
        this.numberOfWin = numberOfWin;
    }

    public int getNumberOfFail() {
        return numberOfFail;
    }

    public void setNumberOfFail(int numberOfFail) {
        this.numberOfFail = numberOfFail;
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }

    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

    public int getAchievementsUnlocked() {
        return achievementsUnlocked;
    }

    public void setAchievementsUnlocked(int achievementsUnlocked) {
        this.achievementsUnlocked = achievementsUnlocked;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
