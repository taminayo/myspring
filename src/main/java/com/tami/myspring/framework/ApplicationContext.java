package com.tami.myspring.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import com.tami.myspring.framework.annotation.Component;
import com.tami.myspring.framework.annotation.Autowired;
import com.tami.myspring.framework.annotation.Scope;
import com.tami.myspring.framework.exception.FrameworkException;

public class ApplicationContext {

    private final Map<Class<?>, Object> singletonBeans = new ConcurrentHashMap<>();
    private final Set<Class<?>> componentBeans;

    public ApplicationContext(Class<?> applicationClass) {
        final Reflections reflections = new Reflections(applicationClass.getPackage().getName());
        this.componentBeans = reflections.getTypesAnnotatedWith(Component.class).stream()
            .filter(clazz -> !clazz.isInterface())
            .collect(Collectors.toSet());
    }

    public <T> T getBean(Class<T> clazz) {
        if (!clazz.isInterface()) {
            throw new FrameworkException("Class " + clazz.getName() + " should be an interface");
        }

        final Class<T> implementation = findImplementationByInterface(clazz);
        final Component annotation = implementation.getAnnotation(Component.class);
        if (annotation.scope() == Scope.SINGLETONE) {
            return (T) singletonBeans.computeIfAbsent(clazz, it -> createBean(clazz, implementation));
        }
        return createBean(clazz, implementation);
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> findImplementationByInterface(Class<T> interfaceItem) {
        final Set<Class<?>> classesWithInterfaces = componentBeans.stream()
                .filter(componentBean -> List.of(componentBean.getInterfaces()).contains(interfaceItem))
                .collect(Collectors.toSet());

        if (classesWithInterfaces.size() > 1) {
            throw new FrameworkException("There are more than 1 implementation: " + interfaceItem.getName());
        }

        return (Class<T>) classesWithInterfaces.stream()
                .findFirst()
                .orElseThrow(() -> new FrameworkException("The is no class with interface: " + interfaceItem));
    }

    private <T> T createBean(Class<T> clazz, Class<T> implementation) {
        try {
            final Constructor<T> constructor = findConstructor(implementation);
            final Object[] parameters = findConstructorParameters(constructor);
            final T bean = constructor.newInstance(parameters);

            final Object proxy = Proxy.newProxyInstance(
                ApplicationContext.class.getClassLoader(),
                new Class[]{clazz},
                new ProxyHandler(bean)
            );
            return clazz.cast(proxy);
        } catch (FrameworkException e) {
            throw e;
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> findConstructor(Class<T> clazz) {
        final Constructor<T>[] constructors = (Constructor<T>[]) clazz.getConstructors();
        if (constructors.length == 1) {
            return constructors[0];
        }

        final Set<Constructor<T>> constructorsWithAnnotation = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toSet());

        if (constructorsWithAnnotation.size() > 1) {
            throw new FrameworkException("There are more than 1 constructor with Autowired annotation: " + clazz.getName());
        }

        return constructorsWithAnnotation.stream()
                .findFirst()
                .orElseThrow(() -> new FrameworkException("Cannot find constructor with annotation Autowired: " + clazz.getName()));
    }

    private <T> Object[] findConstructorParameters(Constructor<T> constructor) {
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        return Arrays.stream(parameterTypes)
                .map(this::getBean)
                .toArray(Object[]::new);
    }
}
