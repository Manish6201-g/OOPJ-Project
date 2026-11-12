package com.campusconnect.dao;

import com.campusconnect.interfaces.Searchable;
import com.campusconnect.model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ItemDAO implements Searchable {
    @Override public List<Item> searchByTitle(String title){return searchItems(title,null,null,null);}
    @Override public List<Item> searchByCategory(String category){return searchItems(null,category,null,null);}
    @Override public List<Item> searchByLocation(String location){return searchItems(null,null,location,null);}

    public List<Item> searchItems(String title,String category,String location,String type) {
        List<Item> items=new ArrayList<>();
        StringBuilder sql=new StringBuilder("SELECT * FROM items WHERE 1=1");
        List<String> values=new ArrayList<>();
        if(title!=null&&!title.isBlank()){sql.append(" AND title LIKE ?");values.add("%"+title.trim()+"%");}
        if(category!=null&&!category.equals("All")&&!category.isBlank()){sql.append(" AND category=?");values.add(category);}
        if(location!=null&&!location.isBlank()){sql.append(" AND location LIKE ?");values.add("%"+location.trim()+"%");}
        if(type!=null&&!type.equals("ALL")){sql.append(" AND item_type=?");values.add(type);}
        sql.append(" ORDER BY created_at DESC");
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql.toString())){
            for(int i=0;i<values.size();i++)ps.setString(i+1,values.get(i));
            try(ResultSet rs=ps.executeQuery()){while(rs.next())items.add(mapItem(rs));}
        }catch(SQLException e){throw new RuntimeException("Item search failed: "+e.getMessage(),e);}
        return items;
    }

    public Item getItemById(int id) {
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement("SELECT * FROM items WHERE item_id=?")){
            ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){if(rs.next())return mapItem(rs);}
        }catch(SQLException e){throw new RuntimeException("Unable to load item",e);}return null;
    }

    public boolean addItem(Item item) {
        String sql="INSERT INTO items(title,category,description,location,item_type,status,reported_by,item_date) VALUES(?,?,?,?,?,?,?,?)";
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,item.getTitle());ps.setString(2,item.getCategory());ps.setString(3,item.getDescription());ps.setString(4,item.getLocation());
            ps.setString(5,item.getItemType());ps.setString(6,item.getStatus());ps.setInt(7,item.getReportedBy());
            if(item.getItemDate()==null)ps.setNull(8,Types.DATE);else ps.setDate(8,java.sql.Date.valueOf(item.getItemDate()));
            return ps.executeUpdate()==1;
        }catch(SQLException e){throw new RuntimeException("Item submission failed: "+e.getMessage(),e);}
    }

    public int getTotalItems(){return count(null);} public int getLostItemsCount(){return count("LOST");} public int getFoundItemsCount(){return count("FOUND");}
    private int count(String type){String sql=type==null?"SELECT COUNT(*) FROM items":"SELECT COUNT(*) FROM items WHERE item_type=?";
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql)){if(type!=null)ps.setString(1,type);try(ResultSet rs=ps.executeQuery()){rs.next();return rs.getInt(1);}}
        catch(SQLException e){throw new RuntimeException("Item count failed: "+e.getMessage(),e);}}

    private Item mapItem(ResultSet rs)throws SQLException {
        int id=rs.getInt("item_id");String type=rs.getString("item_type");java.sql.Date d=rs.getDate("item_date");LocalDate date=d==null?null:d.toLocalDate();
        if("LOST".equals(type))return new LostItem(id,rs.getString("title"),rs.getString("category"),rs.getString("description"),rs.getString("location"),rs.getString("status"),rs.getInt("reported_by"),date);
        return new FoundItem(id,rs.getString("title"),rs.getString("category"),rs.getString("description"),rs.getString("location"),rs.getString("status"),rs.getInt("reported_by"),date);
    }
}
