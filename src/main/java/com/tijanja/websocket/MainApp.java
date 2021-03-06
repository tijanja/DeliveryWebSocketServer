/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.tijanja.websocket;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.tijanja.model.Location;
import com.tijanja.model.LocationResponse;
import com.tijanja.model.LoginObject;
import com.tijanja.model.LoginResponse;
import com.tijanja.model.Response;
import com.tijanja.model.User;
import com.uber.h3core.H3Core;
import com.uber.h3core.util.GeoCoord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@ServerEndpoint("/endpoint")
public class MainApp {

    HashMap<String, User> clientSession;
    Gson gson;
    Connection conn;

    final static Logger logger = LoggerFactory.getLogger(MainApp.class);
    

    public MainApp() {
        System.out.println("class loaded " + this.getClass());
        logger.info("Hello World");
        clientSession = new HashMap<>();
        gson = new Gson();
        conn = ConfigDB.connectDB();

    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.printf("Session opened, id: %s%n", session.getId());

        try {
            session.getBasicRemote().sendText("{'action':'connected','sessionId':'" + session.getId() + "','timeout':'"
                    + session.getMaxIdleTimeout() + "'}");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {

        // session.getId(), message);
        Response res = (Response) gson.fromJson(message, Response.class);

        switch (res.getAction()) {
        case "login": {
            LoginResponse loginRes = gson.fromJson(message, LoginResponse.class);
            LoginObject loginObject = loginRes.getLoginObject();
            try {
                login(session, loginObject);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (EncodeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        }

        case "logLocation": {
            System.out.println("log: "+message);
            LocationResponse locationRes = gson.fromJson(message, LocationResponse.class);
            try {
                //System.out.println(clientSession.get(locationRes.getSessionId()).getEmail());
                H3(locationRes.getLocation().getLat(), locationRes.getLocation().getLng(), 9, clientSession.get(locationRes.getSessionId()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        }
        
        case "getAllOnlineRider":{
            System.out.print(message+"-----");
            try {
                session.getBasicRemote().sendText(getAllOnlineRider());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            break;
        }
     
    
    }
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        clientSession.remove(session.getId());
        System.out.printf("Session closed with id: %s%n", session.getId());
    }

    public void createNewCustomer()
    {

    }

    public void createNewDriver(Session session, LoginObject loginObject) {
        try {
            if (conn != null) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("Insert into user(sessionid,fname,lname,userid,password,flag) Values('"
                        + session.getId() + "','" + loginObject.getEmail() + "','tunji','akinde','"
                        + loginObject.getPassword() + "','0');");

            } else {
                System.out.print("not connected to db");
            }
        } catch (SQLException e) {

            System.out.print(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void login(Session session, LoginObject loginObject) throws IOException, EncodeException
    {
        
        try {

            switch(loginObject.getUserType())
            {
                case "driver":
                {
                    loginDriver(session,loginObject);
                    break;
                }

                case "customer":
                {
                    loginCustomer(session,loginObject);
                    break;
                }
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loginDriver(Session session, LoginObject loginObject) throws SQLException, IOException
    {
        HashMap<String,Object> payload = new HashMap<>();
        Statement statement = conn.createStatement();
            ResultSet rst = statement.executeQuery("Select * from user where fname='"+loginObject.getEmail()+"' AND password='"+loginObject.getPassword()+"'");
        
            
            User user = new User();
            user.setSession(session);

            while (rst.next()) {
                user.setSessionId(session.getId());
                user.setId(rst.getString("userid"));
                user.setEmail(rst.getString("fname"));
              }


              if(user.getEmail().equalsIgnoreCase(loginObject.getEmail()))
                {
                    clientSession.put(session.getId(), user);
                
                    payload.put("session", session.getId());
                    payload.put("action","success");
                    payload.put("type","login");
                    session.getBasicRemote().sendText(gson.toJson(payload)); 
                }
                else
                {
                    payload.put("action","error");
                    payload.put("type","login");
                    session.getBasicRemote().sendText(gson.toJson(payload));
                }
    }

    public void loginCustomer(Session session, LoginObject loginObject)
    {

    }

    public void H3(double lat,double lng,int res,User user) throws IOException
    {
        H3Core h3 = H3Core.newInstance();
        String hexAddr = h3.geoToH3Address(lat, lng, res);
        long hexLong = h3.stringToH3(hexAddr);
        List<String> longs = h3.kRing(hexAddr, 1);
        java.util.List<GeoCoord> geoCoords = h3.h3ToGeoBoundary(longs.get(0));

        List<String> hexArray = h3.h3ToChildren(hexAddr, 10);

        try {
            updateDriverLocation(hexArray,user);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //session.getBasicRemote().sendText(hexArray.toString());
        //System.out.println(hexArray);

        // for(String f : hexArray)
        // {
        //     System.out.println(f);
        // }
 
    }

    public void updateDriverLocation(List<String> hexA,User user) throws SQLException
    {

        try
        {
            if(conn != null)
            {
                Statement st = conn.createStatement();
                ResultSet rst = st.executeQuery("Select userid from onlineDriver where userid='"+user.getEmail()+"';");

                if(rst.next())
                {
                    Statement statement = conn.createStatement();
                    statement.executeUpdate("UPDATE onlineDriver set hexDriver='"+hexA.get(0)+"', hexNear='"+hexA.toString()+"', created=now() WHERE userid='"+user.getEmail()+"';");
                    user.setLocation(hexA.get(0));
                }
                else
                {
                        Statement statement = conn.createStatement();
                        statement.executeUpdate("INSERT INTO onlineDriver(userid,hexDriver,hexNear) VALUES('"+user.getEmail()+"','"+hexA.get(0)+"','"+hexA.toString()+"');");
                        user.setLocation(hexA.get(0));
                }
            }
            else
            {
                conn = ConfigDB.connectDB();
            }
        }
        catch(SQLNonTransientConnectionException e)
        {
            conn = ConfigDB.connectDB();
        }
        
    }

    public String getAllOnlineRider() throws SQLException
    {
        Statement st = conn.createStatement();
        ResultSet rst = st.executeQuery("Select hexDriver from onlineDriver;");
        HashMap<String,String> paylooad = new HashMap<>();
        paylooad.put("action", "getAllOnlineRider");
        while(rst.next())
        {
            paylooad.put("hex", rst.getString("hexDriver").trim());
        }

        return gson.toJson(paylooad);
        
    }
}
