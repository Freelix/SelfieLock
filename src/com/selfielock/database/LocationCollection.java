package com.selfielock.database;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.selfielock.location.GeoLocation;
import com.selfielock.utils.SLUtils;

public class LocationCollection implements Serializable {

private static final long serialVersionUID = 1L;
    
    private List<GeoLocation> listLocations;
    private String userEmail;
    
    // Constructors
    public LocationCollection(String userEmail, List<GeoLocation> list)
    {
        this.listLocations = list;
        this.userEmail = userEmail; 
    }
    
    public LocationCollection()
    {
        this.userEmail = "";
        this.listLocations = new ArrayList<GeoLocation>();
    }
    
    // Get / Set
    public List<GeoLocation> getListLocations() {
        return listLocations;
    }

    public void setListLocations(List<GeoLocation> listLocations) {
        this.listLocations = listLocations;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    // Functions
    public void addFromString(String strAc, String email) 
    {
        try {
            LocationCollection lc = (LocationCollection) SLUtils.fromString(strAc);
            this.listLocations = lc.listLocations;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.userEmail = email;
    }

}
