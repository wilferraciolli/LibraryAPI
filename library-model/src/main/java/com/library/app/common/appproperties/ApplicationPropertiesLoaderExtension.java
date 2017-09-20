package com.library.app.common.appproperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

/**
 * The type Application properties loader extension. This class is used as an CDI extension to intercept @PropertyValue annotation
 * and read property values from the application.properties file. This class will be run for every managed bean on the application.
 */
public class ApplicationPropertiesLoaderExtension implements Extension {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Properties properties;

    /**
     * Initialize property values.
     *
     * @param <T> the type parameter
     * @param pit the pit
     */
    public <T> void initializePropertyValues(@Observes final ProcessInjectionTarget<T> pit) {

        //capture the injection
        final AnnotatedType<T> at = pit.getAnnotatedType();
        final InjectionTarget<T> it = pit.getInjectionTarget();

        //create the wrapper and ovveride its methods
        final InjectionTarget<T> wrapper = new InjectionTarget<T>() {
            @Override
            public T produce(final CreationalContext<T> ctx) {
                return it.produce(ctx);
            }

            @Override
            public void dispose(final T instance) {
                it.dispose(instance);
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return it.getInjectionPoints();
            }

            @Override
            public void inject(final T instance, final CreationalContext<T> ctx) {
                //this is the main method that we want to change the default behaviour
                it.inject(instance, ctx);

                //check if any of the fields have the annotation
                for (final Field field : at.getJavaClass().getDeclaredFields()) {
                    //check that the field has the annotation of @PropertyValue
                    final PropertyValue annotation = field.getAnnotation(PropertyValue.class);
                    if (annotation != null) {
                        final String propertyName = annotation.name();
                        logger.debug("Setting property {} into field {}", propertyName, field.getName());
                        //set field to accessible
                        field.setAccessible(true);
                        final Class<?> fieldType = field.getType();

                        try {
                            if (fieldType == Integer.class) {
                                //get the value and convert it onto Integer
                                final String value = getPropertyValue(propertyName);
                                field.set(instance, Integer.valueOf(value));
                                logger.debug("Value of the field {} set with value {}", field.getName(), value);
                            } else {
                                logger.warn("Type of field not supported: {}", fieldType);
                            }
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            logger.error("Error while setting property into field", e);
                        }
                    }
                }
            }

            @Override
            public void postConstruct(final T instance) {
                it.postConstruct(instance);
            }

            @Override
            public void preDestroy(final T instance) {
                it.preDestroy(instance);
            }
        };

        //change the injection target to use the wrapper created above
        pit.setInjectionTarget(wrapper);
    }

    /**
     * Gets property value.
     *
     * @param propertyName the property name
     * @return the property value
     */
    private String getPropertyValue(final String propertyName) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty(propertyName);
    }
}
