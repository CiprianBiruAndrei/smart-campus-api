/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework.exceptions.mappers;

import com.mycompany.coursework.exceptions.RoomNotEmptyException;
import com.mycompany.coursework.models.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        ErrorMessage error = new ErrorMessage(
                409,
                "Conflict",
                exception.getMessage()
        );

        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}