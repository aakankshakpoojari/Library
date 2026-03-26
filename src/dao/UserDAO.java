package dao;

import db.DBConnection;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (id, name) VALUES (?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.executeUpdate();
    }

    public void removeUser(String id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, id);
        ps.executeUpdate();
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        Statement st = DBConnection.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            users.add(new User(rs.getString("id"), rs.getString("name")));
        }
        return users;
    }

    public boolean exists(String id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }
}