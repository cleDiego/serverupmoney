/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import model.ErrorMessager;
import ws.exception.UnauthenticatedException;

@Provider
public class UnauthenticatedExceptionMapper implements ExceptionMapper<UnauthenticatedException>{

    //Envia para o cliente um json com o status UNAUTHORIZED
    @Override
    public Response toResponse(UnauthenticatedException exception) { 
   
        ErrorMessager jsonError = new ErrorMessager();
        jsonError.setErro(exception.getMessage());
        jsonError.setStatusCode(Response.Status.UNAUTHORIZED.getStatusCode());
        jsonError.setStatusMessage(Response.Status.UNAUTHORIZED.toString());
        
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(jsonError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
}
