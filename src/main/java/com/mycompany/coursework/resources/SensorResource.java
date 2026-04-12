/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.coursework.resources;

import com.mycompany.coursework.exceptions.LinkedResourceNotFoundException;
import com.mycompany.coursework.models.Room;
import com.mycompany.coursework.models.Sensor;
import com.mycompany.coursework.models.SensorReading;
import com.mycompany.coursework.storage.MemoryStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @POST
    public Response createSensor(Sensor sensor) {

        if (sensor == null || sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Sensor ID is required")
                    .build();
        }

        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Room ID is required")
                    .build();
        }

        Room room = MemoryStore.rooms.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException("The referenced room does not exist, so the sensor cannot be created.");
        }

        MemoryStore.sensors.put(sensor.getId(), sensor);

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<String>());
        }

        room.getSensorIds().add(sensor.getId());

        if (MemoryStore.sensorReadings.get(sensor.getId()) == null) {
            MemoryStore.sensorReadings.put(sensor.getId(), new ArrayList<SensorReading>());
        }

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    @GET
    public Response getSensors(@QueryParam("type") String type) {

        Collection<Sensor> allSensors = MemoryStore.sensors.values();

        if (type == null || type.trim().isEmpty()) {
            return Response.ok(allSensors).build();
        }

        List<Sensor> filteredSensors = new ArrayList<Sensor>();

        for (Sensor sensor : allSensors) {
            if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(sensor);
            }
        }

        return Response.ok(filteredSensors).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}