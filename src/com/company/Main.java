package com.company;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;

public class Main {
    static boolean isMessage = true;
    static boolean isMouseEvent = false;
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress("192.168.137.1",6666);
            serverSocket.bind(inetSocketAddress);
            System.out.println("Listening to connections on Address : "+serverSocket.getLocalSocketAddress());
            Socket socket=serverSocket.accept();//establishes connection and waits for the client
            System.out.println("Connection established: "+socket);
            boolean connected = true;
            while(connected){
                DataInputStream dis=new DataInputStream(socket.getInputStream());
                String  str=(String)dis.readUTF();
                if(str.equalsIgnoreCase("message")){
                    System.out.println(str+ " mode activated");
                    isMessage = true;
                    isMouseEvent = false;
                }else if(str.equalsIgnoreCase("mouse")){
                    System.out.println(str+ " mode activated");
                    isMessage = false;
                    isMouseEvent = true;
                }
                if(str.equalsIgnoreCase("double")){
                    try {
                        Robot robot = new Robot();
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                }
                if(str.equalsIgnoreCase("single")){
                    try {
                        Robot robot = new Robot();
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                }
                if (str.equalsIgnoreCase("stop")){
                    connected = false;
                }

                if(isMessage){
                    System.out.println(str);
                }else if(isMouseEvent){
                    try {
                        Robot robot = new Robot();
                        String[] positions = str.split(",");
                        if(positions.length>0) {
                            int x = Integer.parseInt(positions[0]);
                            int y = Integer.parseInt(positions[1]);
                            Point p = MouseInfo.getPointerInfo().getLocation();
                            int currentx = p.x;
                            int currenty = p.y;
                            if (Math.abs(x) < 15 && Math.abs(y) < 15) {
                                robot.mouseMove(currentx + x, currenty + y);
                            }
                        }else{

                        }

                    } catch (AWTException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException ne){

                    }
                }
            }


            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
