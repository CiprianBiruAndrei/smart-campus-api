/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework.storage;

import com.mycompany.coursework.models.Room;
import com.mycompany.coursework.models.Sensor;
import com.mycompany.coursework.models.SensorReading;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryStore {

    public static Map<String, Room> rooms = new HashMap<String, Room>();
    public static Map<String, Sensor> sensors = new HashMap<String, Sensor>();
    public static Map<String, List<SensorReading>> sensorReadings = new HashMap<String, List<SensorReading>>();

    private MemoryStore() {
    }
}