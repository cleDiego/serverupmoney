package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author amand
 */
public class JPAUtil {
    
    private static EntityManagerFactory emf;
    
    public static EntityManager getEntityManager(){
        
        if (emf == null){
            emf = Persistence.createEntityManagerFactory("upMoneyTeste");
}
        
        return emf.createEntityManager();
    }
    
}
