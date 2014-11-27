package com.selfielock.database;

import com.selfielock.utils.Constants;

public class UserEntity {
    
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private byte[] image;
    private String password;
    
    public UserEntity(String firstName, String lastName, String gender, String email, byte[] image, String password)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.setImage(image);
        this.setPassword(password);
    }
    
    public UserEntity() 
    {
        this.firstName = "";
        this.lastName = "";
        this.gender = Constants.MAN;
        this.email = "";
        this.setImage(null);
        this.setPassword("");
    }

    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }   
}
