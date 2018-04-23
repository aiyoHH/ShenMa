package cn.javava.shenma.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Copyright © 2017 Zego. All rights reserved.
 */

public class Room implements Serializable {

    public int number;

    public String roomName;

    public String roomID;
    public int room_id;
    public int balance;
    public boolean isData;
    public ArrayList<String> streamList = new ArrayList<>();

    public String roomIcon;

    @Override
    public String toString() {
        return "Room{" +
                "number=" + number +
                ", roomName='" + roomName + '\'' +
                ", roomID=" + roomID +
                ", balance=" + balance +
                ", isData=" + isData +
                ", streamList=" + streamList +
                ", roomIcon='" + roomIcon + '\'' +
                '}';
    }
}
