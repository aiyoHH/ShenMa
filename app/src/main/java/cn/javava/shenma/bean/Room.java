package cn.javava.shenma.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Copyright © 2017 Zego. All rights reserved.
 */

public class Room implements Serializable {

    public String roomName;

    public String roomID;

    public ArrayList<String> streamList = new ArrayList<>();

    public int roomIcon;

    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                ", roomID='" + roomID + '\'' +
                ", streamList=" + streamList +
                ", roomIcon=" + roomIcon +
                '}';
    }
}
