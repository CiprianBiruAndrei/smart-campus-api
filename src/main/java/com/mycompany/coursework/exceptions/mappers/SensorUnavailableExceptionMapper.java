/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework.exceptions.mappers;

import com.mycompany.coursework.exceptions.SensorUnavailableException;
import com.mycompany.coursework.models.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorMessage error = new ErrorMessage(
                403,
                "Forbidden",
                exception.getMessage()
        );

        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}