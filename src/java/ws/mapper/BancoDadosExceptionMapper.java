/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import model.ErrorMessager;
import ws.exception.BancoDadosException;

/**
 *
 * @author amand
 */
public class BancoDadosExceptionMapper implements ExceptionMapper<BancoDadosException>{

    @Override
    public Response toResponse(BancoDadosException e) {
       ErrorMessager jsonError = new ErrorMessager();
        jsonError.setErro(e.getMessage());
        jsonError.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        jsonError.setStatusMessage(Response.Status.BAD_REQUEST.toString());
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(jsonError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
}
