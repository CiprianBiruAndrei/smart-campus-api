/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.coursework.exceptions.mappers;

import com.mycompany.coursework.models.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        ErrorMessage error = new ErrorMessage(
                500,
                "Internal Server Error",
                "An unexpected error occurred. Please contact the administrator."
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}