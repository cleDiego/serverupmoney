package ws.filter;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

//Testando mas sem funcionar ainda
//Implementado conforme aulas do Razer
@Provider
@PreMatching
public class CorsFilter implements ContainerResponseFilter {
    
    public void filter(@Context ContainerRequestContext request) throws IOException {
        if (isPreflightRequest(request)) {
            request.abortWith(Response.ok().build());
        }
    }
    private static boolean isPreflightRequest (@Context ContainerRequestContext request) {
        return request.getHeaderString("Origin") != null
                && request.getMethod().equalsIgnoreCase("OPTIONS");
    }
    @Override
    public void filter(@Context ContainerRequestContext request, @Context ContainerResponseContext response) throws IOException {
        if(request.getHeaderString("Origin") == null) { //Não é CORS
            return;
        }
        if (isPreflightRequest(request)) {
            response.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization," + "xxx-header");
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            response.getHeaders().add("Access-Control-Max-Age", "1209600");
        }
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
    }
}
