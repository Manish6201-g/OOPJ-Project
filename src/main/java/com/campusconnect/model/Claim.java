package com.campusconnect.model;
import java.time.LocalDateTime;
public class Claim {
    private int claimId,itemId,userId; private String claimDescription,status; private LocalDateTime claimDate;
    public Claim(int claimId,int itemId,int userId,String description,String status,LocalDateTime date){this.claimId=claimId;this.itemId=itemId;this.userId=userId;this.claimDescription=description;this.status=status;this.claimDate=date;}
    public int getClaimId(){return claimId;} public int getItemId(){return itemId;} public int getUserId(){return userId;}
    public String getClaimDescription(){return claimDescription;} public String getStatus(){return status;} public LocalDateTime getClaimDate(){return claimDate;}
    public void setStatus(String status){this.status=status;}
}
