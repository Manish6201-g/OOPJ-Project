package com.campusconnect.dao;

import com.campusconnect.model.Claim;
import java.sql.*;
import java.util.*;

public class ClaimDAO {
    public boolean addClaim(Claim claim) {
        String check="SELECT item_type,status,reported_by FROM items WHERE item_id=?";
        String duplicate="SELECT COUNT(*) FROM claims WHERE item_id=? AND user_id=? AND status='PENDING'";
        String insert="INSERT INTO claims(item_id,user_id,claim_description,status) VALUES(?,?,?,'PENDING')";
        try(Connection con=DBConnection.getConnection()){
            con.setAutoCommit(false);
            try(PreparedStatement ps=con.prepareStatement(check)){
                ps.setInt(1,claim.getItemId());try(ResultSet rs=ps.executeQuery()){
                    if(!rs.next()||!"FOUND".equals(rs.getString("item_type"))||!"OPEN".equals(rs.getString("status"))||rs.getInt("reported_by")==claim.getUserId())throw new IllegalArgumentException("Only an open found item reported by another user can be claimed.");
                }
            }
            try(PreparedStatement ps=con.prepareStatement(duplicate)){ps.setInt(1,claim.getItemId());ps.setInt(2,claim.getUserId());try(ResultSet rs=ps.executeQuery()){rs.next();if(rs.getInt(1)>0)throw new IllegalArgumentException("You already have a pending claim for this item.");}}
            try(PreparedStatement ps=con.prepareStatement(insert)){ps.setInt(1,claim.getItemId());ps.setInt(2,claim.getUserId());ps.setString(3,claim.getClaimDescription());ps.executeUpdate();}
            con.commit();return true;
        }catch(SQLException e){throw new RuntimeException("Claim submission failed: "+e.getMessage(),e);}
    }

    public List<Object[]> getClaimsByUserId(int userId) {
        String sql="SELECT c.claim_id,i.title,i.item_type,c.claim_description,c.status,c.claim_date FROM claims c JOIN items i ON c.item_id=i.item_id WHERE c.user_id=? ORDER BY c.claim_date DESC";
        List<Object[]> rows=new ArrayList<>();
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql)){ps.setInt(1,userId);try(ResultSet rs=ps.executeQuery()){while(rs.next())rows.add(new Object[]{rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getTimestamp(6)});}}
        catch(SQLException e){throw new RuntimeException("Unable to load your claims: "+e.getMessage(),e);}return rows;
    }

    public List<Object[]> getAllClaimsForAdmin() {
        String sql="SELECT c.claim_id,i.title,u.name,u.email,c.claim_description,c.status,c.claim_date FROM claims c JOIN items i ON c.item_id=i.item_id JOIN users u ON c.user_id=u.user_id ORDER BY CASE c.status WHEN 'PENDING' THEN 0 ELSE 1 END,c.claim_date DESC";
        List<Object[]> rows=new ArrayList<>();
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql);ResultSet rs=ps.executeQuery()){
            while(rs.next())rows.add(new Object[]{rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getTimestamp(7)});
        }catch(SQLException e){throw new RuntimeException("Unable to load admin claims: "+e.getMessage(),e);}return rows;
    }

    public void updateClaimStatus(int claimId,String status) {
        if(!"APPROVED".equals(status)&&!"REJECTED".equals(status))throw new IllegalArgumentException("Invalid claim decision.");
        String find="SELECT item_id,status FROM claims WHERE claim_id=? FOR UPDATE";
        try(Connection con=DBConnection.getConnection()){
            con.setAutoCommit(false);int itemId;
            try(PreparedStatement ps=con.prepareStatement(find)){ps.setInt(1,claimId);try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())throw new IllegalArgumentException("Claim not found.");
                if(!"PENDING".equals(rs.getString("status")))throw new IllegalArgumentException("This claim has already been reviewed.");itemId=rs.getInt("item_id");}}
            if("APPROVED".equals(status)){
                try(PreparedStatement ps=con.prepareStatement("UPDATE items SET status='CLAIMED' WHERE item_id=? AND status='OPEN'")){ps.setInt(1,itemId);if(ps.executeUpdate()!=1)throw new IllegalArgumentException("Item is no longer open; claim cannot be approved.");}
            }
            try(PreparedStatement ps=con.prepareStatement("UPDATE claims SET status=? WHERE claim_id=? AND status='PENDING'")){ps.setString(1,status);ps.setInt(2,claimId);if(ps.executeUpdate()!=1)throw new IllegalArgumentException("Claim could not be updated.");}
            con.commit();
        }catch(SQLException e){throw new RuntimeException("Could not update claim: "+e.getMessage(),e);}
    }
}
