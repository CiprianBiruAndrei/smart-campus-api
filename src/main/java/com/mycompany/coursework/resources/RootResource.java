/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework.resources;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1")
public class RootResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getDiscovery() {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("apiName", "Smart Campus Sensor & Room Management API");
        response.put("version", "v1");

        Map<String, String> adminContact = new LinkedHashMap<>();
        adminContact.put("name", "Campus Facilities API Support");
        adminContact.put("email", "facilities@smartcampus.local");
        response.put("adminContact", adminContact);

        Map<String, String> collections = new LinkedHashMap<>();
        collections.put("rooms", "/api/v1/rooms");
        collections.put("sensors", "/api/v1/sensors");
        response.put("collections", collections);

        return response;
    }
}