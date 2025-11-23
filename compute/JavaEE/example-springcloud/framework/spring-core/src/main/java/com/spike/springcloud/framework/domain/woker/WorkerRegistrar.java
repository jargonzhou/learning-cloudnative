package com.spike.springcloud.framework.domain.woker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Worker registrar.
 *
 * @see BeanDefinitionBuilder#genericBeanDefinition()
 * @see BeanDefinitionReaderUtils#registerBeanDefinition(BeanDefinitionHolder, BeanDefinitionRegistry)
 */
@Slf4j
public class WorkerRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        // resource binder to load properties
        Binder binder = new Binder(
                ConfigurationPropertySources.from(((ConfigurableEnvironment) environment).getPropertySources()),
                new PropertySourcesPlaceholdersResolver(environment));
        WorkerProperties properties = binder.bind(IWorker.CONFIG_PREFIX, WorkerProperties.class).get();

        properties.getConfigs().entrySet().stream()
                .filter(map -> null == map.getValue().getEnabled() || map.getValue().getEnabled())
                .forEach(map -> {
                    String name = map.getKey();
                    WorkerProperties.WorkerConfig worker = map.getValue();

                    if (worker.getType() == null) {
                        log.warn("Empty type when register worker: {}", name);
                        return;
                    }

                    // create bean definition from factory bean
                    log.info("Register worker: {}", name);
                    BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(WorkerFactoryBean.class);
                    try {
                        bdb.addPropertyValue("name", worker.getName());
                        bdb.addPropertyValue("type",
                                Class.forName(worker.getType(), true, this.getClass().getClassLoader()));
                        bdb.addPropertyValue("params", worker.getParams());
                        bdb.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                    } catch (ClassNotFoundException e) {
                        log.error("Class " + worker.getType() + " not found!", e);
                    }
                    AbstractBeanDefinition bean = bdb.getBeanDefinition();

                    // register bean definition
                    BeanDefinitionHolder holder = new BeanDefinitionHolder(
                            bean, IWorker.BEAN_NAME_PREFIX + worker.getName());
                    BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
                });
    }
}
