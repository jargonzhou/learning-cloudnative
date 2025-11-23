package com.spike.springcloud.framework.domain.woker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.Map;

/**
 * Worker factory bean.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WorkerFactoryBean extends AbstractFactoryBean<IWorker> {

    // worker implementation class
    private Class<? extends IWorker> type;
    // worker name
    private String name;
    // worker parameters
    private Map<String, Object> params;

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    protected IWorker createInstance() throws Exception {
        IWorker result = type.getDeclaredConstructor().newInstance();
        result.setName(name);
        result.init(params);
        return result;
    }
}
