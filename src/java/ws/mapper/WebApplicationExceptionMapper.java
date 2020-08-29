/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.mapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import model.ErrorMessager;

/*
    Mapper para demais exceções 
    WebApplicationException: é a exceção mais abrangente do Jersey
    Para respostas mais amigáveis as aplicações clientes
*/
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException>{

    @Override
    public Response toResponse(WebApplicationException exception) {
        
        ErrorMessager jsonError = new ErrorMessager();
        jsonError.setErro(exception.getMessage());
        jsonError.setStatusCode(exception.getResponse().getStatus());
        jsonError.setStatusMessage(exception.getResponse().getStatusInfo().toString());
        
      return Response.status(exception.getResponse().getStatus())
              .entity(jsonError)
              .type(MediaType.APPLICATION_JSON)
              .build();
    }
    
}
