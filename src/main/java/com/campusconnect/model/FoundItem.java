package com.campusconnect.model;
import java.time.LocalDate;
public class FoundItem extends Item {
    public FoundItem(int id,String title,String category,String description,String location,String status,int reportedBy,LocalDate date){super(id,title,category,description,location,status,reportedBy,date);}
    @Override public String getItemType(){return "FOUND";}
}
