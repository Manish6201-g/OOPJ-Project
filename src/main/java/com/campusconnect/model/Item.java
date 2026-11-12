package com.campusconnect.model;
import java.time.LocalDate;
public abstract class Item {
    protected int itemId; protected String title, category, description, location, status; protected int reportedBy; protected LocalDate itemDate;
    public Item(int itemId,String title,String category,String description,String location,String status,int reportedBy,LocalDate itemDate){
        this.itemId=itemId;this.title=title;this.category=category;this.description=description;this.location=location;this.status=status;this.reportedBy=reportedBy;this.itemDate=itemDate;
    }
    public abstract String getItemType();
    public void displayDetails(){System.out.println(getItemType()+": "+title+" | "+category+" | "+location+" | "+status);}
    public int getItemId(){return itemId;} public String getTitle(){return title;} public String getCategory(){return category;}
    public String getDescription(){return description;} public String getLocation(){return location;} public String getStatus(){return status;}
    public int getReportedBy(){return reportedBy;} public LocalDate getItemDate(){return itemDate;}
}
