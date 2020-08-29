
import java.text.ParseException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author brunajeniferf
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
       
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("upMoneyTeste");
        
        EntityManager entityManager = entityManagerFactory.createEntityManager();
          
        //inserirlimiteSubcategoria(entityManager);
       // testeTupla();
        //buscaDespesaByMes();
        //testaAnaliseMensal();
        //testaPorcentagem();
        entityManager.close();
        entityManagerFactory.close();

    }
}
    