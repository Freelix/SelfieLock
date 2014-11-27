package com.selfielock.achievement;

import java.io.Serializable;

import com.selfielock.R;

public class Achievement implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    private int img;
    private String description;
    private int id;
    private boolean unlocked;
    
    private static int nextId = 0;
    
    // Constructors
    public Achievement(){
        super();
    }
    
    public Achievement(String description, boolean unlocked)
    {
        this.description = description;
        this.unlocked = unlocked;
        this.img = R.drawable.medal_silver;
    }
    
    public Achievement(int img, String description) {
        super();
        
        this.id = nextId;
        this.img = img;
        this.description = description;
        this.unlocked = false;
        
        nextId++;
    }

    // Get, Set
    public int getImg() {
    	return img;
    }
    
    public void setImg(int img) {
    	this.img = img;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}
