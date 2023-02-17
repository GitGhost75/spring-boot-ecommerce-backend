package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import java.util.Arrays;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Value("${allowed.origins}")
    private String[] origins;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH};

        // get a list of all entity classes form the entity manager
        //Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        //Class<?> classes[] = entities.stream().map(e -> e.getJavaType()).toArray(Class[]::new);
        Class<?> classes[] = {State.class, Product.class, ProductCategory.class, Country.class, Order.class};

        // disable HTTP methods for Product: PUT, POST and DELETE
        disableHttpMethods(config, theUnsupportedActions, classes);

        // expose  the ids
        exposeIds(config, classes);

        cors.addMapping(config.getBasePath()+"/**").allowedOrigins(origins);
    }

    private void disableHttpMethods(RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions, Class<?>... theClasses) {

        Arrays.stream(theClasses).forEach(theClass -> {
            config.getExposureConfiguration().forDomainType(theClass)
                    .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                    .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
        });
    }

    private void exposeIds(RepositoryRestConfiguration config, Class<?>... theClasses) {

        // expose entity ids for the array of entity/domain types
        Arrays.stream(theClasses).forEach(theClass -> {
            config.exposeIdsFor(theClass);
        });
    }
}
