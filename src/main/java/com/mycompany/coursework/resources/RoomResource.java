/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework.resources;

import com.mycompany.coursework.exceptions.RoomNotEmptyException;
import com.mycompany.coursework.models.Room;
import com.mycompany.coursework.storage.MemoryStore;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Collection<Room> getRooms() {
        return MemoryStore.rooms.values();
    }

    @POST
    public Response createRoom(Room room) {
        if (room == null || room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Room ID is required")
                    .build();
        }

        MemoryStore.rooms.put(room.getId(), room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = MemoryStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Room not found")
                    .build();
        }

        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = MemoryStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Room not found")
                    .build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("This room is currently occupied by active hardware and cannot be deleted.");
        }

        MemoryStore.rooms.remove(roomId);

        return Response.ok("Room deleted successfully").build();
    }
}