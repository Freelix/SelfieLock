package com.selfielock.achievement;

public enum CustomToastPosition {

    PositionTop("1"),
    PositionBottom("2");
    
    private String position;
    
    public String getPosition()
    {
        return position;
    }
    
    CustomToastPosition(String position)
    {
        this.position = position;
    }

}
