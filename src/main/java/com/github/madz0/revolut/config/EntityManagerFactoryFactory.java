package com.github.madz0.revolut.config;

import lombok.Setter;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Supplier;

@Setter
public class EntityManagerFactoryFactory implements Supplier<EntityManagerFactory> {
    //constructor parameter did't work
    @Config("jpa.unit")
    private String unit;

    private EntityManagerFactory provide() {
        return Persistence.createEntityManagerFactory(unit);
    }

    @Override
    public EntityManagerFactory get() {
        return provide();
    }
}
