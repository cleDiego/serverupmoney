package ws.resource;
import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("/server")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.filter.CorsFilter.class);
        resources.add(ws.filter.JwtAuthenticationFilter.class);
        resources.add(ws.mapper.UnauthenticatedExceptionMapper.class);
        resources.add(ws.resource.CategoriaResource.class);
        resources.add(ws.resource.ContaResource.class);
        resources.add(ws.resource.DespesaResource.class);
        resources.add(ws.resource.LimiteResource.class);
        resources.add(ws.resource.LoginJWTResource.class);
        resources.add(ws.resource.ReceitaResource.class);
        resources.add(ws.resource.RelatorioResource.class);
        resources.add(ws.resource.UsuarioResource.class);
    }
    
}
