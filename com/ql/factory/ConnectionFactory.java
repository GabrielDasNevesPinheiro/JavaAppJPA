
package com.ql.factory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import lombok.Getter;

public class ConnectionFactory {
    
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("empresa");
    
    
    public EntityManagerFactory getConnection () {
        return ConnectionFactory.factory;
    }
    
}
