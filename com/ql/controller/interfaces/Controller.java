package com.ql.controller.interfaces;
import java.util.List;

public interface Controller<type> {
    
    public void salvar(type data);
    public List<type> listar();
    public void deletar(Integer id);
    public type buscar(Integer id);
    
}
