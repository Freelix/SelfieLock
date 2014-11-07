package com.selfielock.bluetooth;

public enum MessageType {
    
    CodeMessage("1"),
    BackgroundImgMessage("2");
    
    private String type;
    
    public String getType()
    {
        return type;
    }
    
    MessageType(String type)
    {
        this.type = type;
    }
}
