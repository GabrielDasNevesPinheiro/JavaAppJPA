package com.ql.controller;

import com.ql.controller.interfaces.Controller;
import com.ql.controller.factory.ConnectionFactory;
import com.ql.model.Funcionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

public class FuncionarioController implements Controller<Funcionario> {

    EntityManagerFactory factory = new ConnectionFactory().getConnection();
    EntityManager manager = factory.createEntityManager();

    @Override
    public void salvar(Funcionario funcionario) {

        try {

            this.manager.getTransaction().begin();

            this.manager.merge(funcionario);
            this.manager.getTransaction().commit();

        } catch (Exception e) {
            this.manager.getTransaction().rollback();
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }

    }

    public List<Funcionario> listar(String orderby) {
        
        List<Funcionario> lista = null;
        try {
            Query q = this.manager.createQuery("select f from Funcionario f, setor s where f.setor = s.id order by s.id " + orderby);
            lista = q.getResultList();

            return lista;
        } catch (Exception e) {
            System.out.println("Erro ao listar: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Funcionario> listar() {

        List<Funcionario> lista = null;

        try {

            lista = this.manager.createQuery("from Funcionario").getResultList();

        } catch (Exception e) {
            this.manager.getTransaction().rollback();
            System.out.println("Erro ao listar: " + e.getMessage());
        }

        return lista;

    }

    @Override
    public Funcionario buscar(Integer id) {
        
        Funcionario func = null;
        try {
            func = manager.find(Funcionario.class, id);
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }

        return func;
    }

    @Override
    public void deletar(Integer id) {
        

        try {
            
            this.manager.getTransaction().begin();
            Funcionario func = this.manager.find(Funcionario.class, id);
            this.manager.remove(func);
            this.manager.getTransaction().commit();
            
        } catch (Exception e) {
            
            this.manager.getTransaction().rollback();
            System.out.println("Erro ao deletar: " + e.getMessage());
        }

    }

}
