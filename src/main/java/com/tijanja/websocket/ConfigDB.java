package com.tijanja.websocket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConfigDB {
    
    public static Connection connectDB()
    {
        Connection con = null;
        try
        { 
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                con=DriverManager.getConnection("jdbc:mysql://206.189.115.158:3306/dinkkyDB","openfire","@K1nd3_*"); 
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            
        }
        finally
        {
            return con;
        }
    }

}