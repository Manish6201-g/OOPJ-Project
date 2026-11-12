package com.campusconnect.dao;

import com.campusconnect.model.User;
import com.campusconnect.service.PasswordUtil;
import java.sql.*;

public class UserDAO {
    public User getUserByEmail(String email) {
        String sql="SELECT user_id,name,email,password,role FROM users WHERE email=?";
        try(Connection con=DBConnection.getConnection(); PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,email);
            try(ResultSet rs=ps.executeQuery()){if(rs.next()) return mapUser(rs);}
        } catch(SQLException e){throw new RuntimeException("Unable to find user: "+e.getMessage(),e);}
        return null;
    }

    public User authenticate(String email,String password) {
        User user=getUserByEmail(email);
        if(user==null || !PasswordUtil.verify(password,user.getPassword())) return null;
        if(PasswordUtil.needsUpgrade(user.getPassword())) updatePassword(user.getUserId(),PasswordUtil.hash(password));
        return user;
    }

    public boolean registerUser(User user) {
        String sql="INSERT INTO users(name,email,password,role) VALUES(?,?,?,'STUDENT')";
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,user.getName());ps.setString(2,user.getEmail());ps.setString(3,PasswordUtil.hash(user.getPassword()));
            return ps.executeUpdate()==1;
        }catch(SQLIntegrityConstraintViolationException e){throw new IllegalArgumentException("This email is already registered.");}
        catch(SQLException e){throw new RuntimeException("Unable to register user: "+e.getMessage(),e);}
    }

    private void updatePassword(int userId,String hash) {
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement("UPDATE users SET password=? WHERE user_id=?")){
            ps.setString(1,hash);ps.setInt(2,userId);ps.executeUpdate();
        }catch(SQLException e){throw new RuntimeException("Could not upgrade password storage",e);}
    }

    private User mapUser(ResultSet rs)throws SQLException {
        return new User(rs.getInt("user_id"),rs.getString("name"),rs.getString("email"),rs.getString("password"),rs.getString("role"));
    }
}
