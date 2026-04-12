/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework.resources;

import com.mycompany.coursework.exceptions.SensorUnavailableException;
import com.mycompany.coursework.models.Sensor;
import com.mycompany.coursework.models.SensorReading;
import com.mycompany.coursework.storage.MemoryStore;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        Sensor sensor = MemoryStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found")
                    .build();
        }

        List<SensorReading> readings = MemoryStore.sensorReadings.get(sensorId);

        if (readings == null) {
            readings = new ArrayList<SensorReading>();
            MemoryStore.sensorReadings.put(sensorId, readings);
        }

        return Response.ok(readings).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = MemoryStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found")
                    .build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("This sensor is in MAINTENANCE state and cannot accept new readings.");
        }

        if (reading == null || reading.getId() == null || reading.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Reading ID is required")
                    .build();
        }

        List<SensorReading> readings = MemoryStore.sensorReadings.get(sensorId);

        if (readings == null) {
            readings = new ArrayList<SensorReading>();
            MemoryStore.sensorReadings.put(sensorId, readings);
        }

        readings.add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}