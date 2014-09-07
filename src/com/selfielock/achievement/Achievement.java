package com.selfielock.achievement;

public class Achievement {
	
	private int img;
    private String description;
        
    // Get, Set
    public int getImg()
    {
    	return img;
    }
    
    public void setImg(int img)
    {
    	this.img = img;
    }
    
    public String getDescription()
    {
    	return description;
    }
    
    public void setDescription(String description)
    {
    	this.description = description;
    }
    
    // Constructors
    public Achievement(){
        super();
    }
    
    public Achievement(int img, String description) {
        super();
        
        this.img = img;
        this.description = description;
    }
}
