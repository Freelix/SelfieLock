package com.selfielock.database;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.selfielock.R;
import com.selfielock.achievement.Achievement;
import com.selfielock.utils.Constants;
import com.selfielock.utils.SLUtils;

public class AchievementCollection implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<Achievement> listAchievements;
    private String userEmail;
    
    public AchievementCollection(String userEmail)
    {
        this.listAchievements = createAllAchievements();
        this.userEmail = userEmail; 
    }
    
    public AchievementCollection(String userEmail, List<Achievement> list)
    {
        this.listAchievements = list;
        this.userEmail = userEmail; 
    }
    
    public AchievementCollection()
    {
        this.userEmail = "";
        this.listAchievements = new ArrayList<Achievement>();
    }
    
    public List<Achievement> getListAchievements() {
        return listAchievements;
    }

    public String getUserEmail() {
        return userEmail;
    }
    
    public void addListAchievements(Achievement achievement)
    {
        this.listAchievements.add(achievement);
    }

    @SuppressWarnings("unchecked")
    public void addFromString(String strAc, String email) 
    {
        try {
            AchievementCollection ac = (AchievementCollection) SLUtils.fromString(strAc);
            this.listAchievements = ac.listAchievements;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.userEmail = email;
    }
    
    private List<Achievement> createAllAchievements()
    {
        List<Achievement> achievements = new ArrayList<Achievement>();
        
        Achievement a = new Achievement(R.drawable.medal_silver, Constants.ACH_WON5GAMES);
        achievements.add(a);
        
        return achievements;
    }
    
    public void unlockedAchievement(int position, boolean unlocked)
    {
        this.listAchievements.get(position).setUnlocked(unlocked);
    }
    
    public void updateAchievement(int position, String description, int imageId, boolean unlocked)
    {
        this.listAchievements.get(position).setDescription(description);
        this.listAchievements.get(position).setImg(imageId);
        this.listAchievements.get(position).setUnlocked(unlocked);
    }
}
