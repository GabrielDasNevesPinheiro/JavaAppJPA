/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ql.controller;

import com.ql.controller.interfaces.Controller;
import com.ql.factory.ConnectionFactory;
import com.ql.model.Funcionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author paralamas
 */
public class FuncionarioController implements Controller<Funcionario> {

    
    
    @Override
    public void salvar(Funcionario funcionario) {
        EntityManagerFactory factory = new ConnectionFactory().getConnection();
        EntityManager manager = factory.createEntityManager();
        
        try {
            
            manager.getTransaction().begin();
            
            manager.merge(funcionario);
            manager.getTransaction().commit();
            
            
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println("Erro ao salvar dados: "+e.getMessage());
        } finally {
            manager.close();
        }
        
    }

    
    
    public List<Funcionario> listar(String orderby) {
        EntityManagerFactory factory = new ConnectionFactory().getConnection();
        EntityManager manager = factory.createEntityManager();
        List<Funcionario> lista = null;
        try {
            Query q = manager.createQuery("select f from Funcionario f, setor s where f.setor = s.id order by s.id "+orderby);
            lista = q.getResultList();
            
            return lista;
        } catch (Exception e) {
            System.out.println("Erro ao listar: "+e.getMessage());
        }
        return lista;
    }
    
    @Override
    public List<Funcionario> listar() {
        EntityManagerFactory factory = new ConnectionFactory().getConnection();
        EntityManager manager = factory.createEntityManager();
        
        List<Funcionario> lista = null;
        
        try {
            
             lista = manager.createQuery("from Funcionario").getResultList();
            
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println("Erro ao listar: "+e.getMessage());
        } finally {
            manager.close();
        }
        
        return lista;
        
    }
    
    @Override
    public Funcionario buscar(Integer id) {
        EntityManagerFactory factory = new ConnectionFactory().getConnection();
        EntityManager manager = factory.createEntityManager();
        Funcionario func = null;
        try {
            func = manager.find(Funcionario.class, id);
        } catch (Exception e) {
            System.out.println("Erro na busca: "+e.getMessage());
        }
        
        return func;
    }

    @Override
    public void deletar(Integer id) {
        
        EntityManagerFactory factory = new ConnectionFactory().getConnection();
        EntityManager manager = factory.createEntityManager();
        
        try {
            manager.getTransaction().begin();
            Funcionario func = manager.find(Funcionario.class, id);
            manager.remove(func);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            System.out.println("Erro ao deletar: "+e.getMessage());
        } finally {
            manager.close();
        }
            
    }
    
}
