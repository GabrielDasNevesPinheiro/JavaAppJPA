
package com.ql.controller;

import com.ql.model.Setor;
import com.ql.controller.interfaces.Controller;
import com.ql.controller.factory.ConnectionFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetorController implements Controller<Setor> {

    @Override
    public void salvar(Setor setor) {
       EntityManagerFactory factory = new ConnectionFactory().getConnection();
        EntityManager manager = factory.createEntityManager();
        
        try {
            
            manager.getTransaction().begin();
            
            if (setor.getId() == null) {
                manager.persist(setor);
            } else {
                manager.merge(setor);
            }
            manager.getTransaction().commit();
            
            
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println("Erro ao salvar dados: "+e.getMessage());
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Setor> listar() {
        EntityManagerFactory factory = new ConnectionFactory().getConnection();
        EntityManager manager = factory.createEntityManager();
        
        List<Setor> lista = null;
        
        try {
            
             lista = manager.createQuery("from setor").getResultList();
            
        } catch (Exception e) {
            
            System.out.println("Erro ao listar: "+e.getMessage());
        
        } finally {
            manager.close();
        }
        
        return lista;
    }
    
    @Override
    public Setor buscar(Integer id) {
        EntityManagerFactory factory = new ConnectionFactory().getConnection();
        EntityManager manager = factory.createEntityManager();
        Setor set = null;
        try {
            set = manager.find(Setor.class, id);
        } catch (Exception e) {
            System.out.println("Erro na busca: "+e.getMessage());
        }
        
        return set;
    }
    
    public Setor buscar(String nome) {
        Setor retorno = null;
        List<Setor> list = this.listar();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNome().equals(nome)) {
               retorno = list.get(i); 
            }
        }
        
        return retorno;
    }

    @Override
    public void deletar(Integer id) {
        EntityManagerFactory factory = new ConnectionFactory().getConnection();
        EntityManager manager = factory.createEntityManager();
        
        try {
            manager.getTransaction().begin();
            Setor setor = manager.find(Setor.class, id);
            manager.remove(setor);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println("Erro ao deletar: "+e.getMessage());
        } finally {
            manager.close();
        }
    }
    
}
