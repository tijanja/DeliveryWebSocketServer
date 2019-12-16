package com.tijanja.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.tijanja.model.Company;
import com.tijanja.websocket.ConfigDB;

import org.apache.log4j.lf5.Log4JLogRecord;

@WebServlet("/register")
public class RegistarCompanyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    Connection connect = ConfigDB.connectDB();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        Company company = gson.fromJson(request.getReader().readLine(), Company.class);
        company.setId(generateUniqueId());

        alert(company.getId());
        try {
            signupCompany(company);
        } catch (SQLException e) {
            alert(e.getMessage());
        }
        response.getWriter().println(company.getCompanyName());
    }

    public void alert(String s) {
        System.out.println(s);
    }

    public void signupCompany(Company company) throws SQLException {
        if (connect != null && !connect.isClosed()) {
            Statement statement = connect.createStatement();
            int resultset = statement.executeUpdate(
                    "INSERT INTO company(companyid,name,email,phone,address,password,status,flag) VALUES('"
                            + company.getId() + "','" + company.getCompanyName() + "','" + company.getEmail() + "','"
                            + company.getPhone() + "','" + company.getAddress() + "','" + company.getPassword() + "','0','0')");
                        
        }
        else{
            alert("error cant connect to db");
        }
        
    }

    private String generateUniqueId()
    {
        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(number);
    }

}

