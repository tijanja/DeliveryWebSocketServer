package com.tijanja.websocket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConfigDB {
    
    

    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://localhost/dinkkyDB?serverTimezone=UTC", "admin", "Pr0j3ct1@e");
            // con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/dinkkyDB","root","Project123");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }
        finally
        {
            return con;
        }
    }

}