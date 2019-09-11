/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.tijanja.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


@ServerEndpoint( "/endpoint")
public class HelloWorldEndpoint {

    List<Session> clientSession;

    public HelloWorldEndpoint() {
        System.out.println("class loaded " + this.getClass());
        clientSession = new ArrayList<>();

        Connection conn = ConfigDB.connectDB();
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.printf("Session opened, id: %s%n", session.getId());
        clientSession.add(session);
        try {
            session.getBasicRemote().sendText("{'action':'connected','sessionId':'"+session.getId()+"'}");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.printf("Message received. Session id: %s Message: %s%n",session.getId(), message);
        try 
        {
            session.getBasicRemote().sendText(String.format("We received your message: %s%n "+session.getId()+" ", message));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable e) {
        System.out.printf(e.getMessage());
    }

    @OnClose
    public void onClose(Session session) {
        System.out.printf("Session closed with id: %s%n", session.getId());
    }
}
