package cn.nyaaar.partridgemngservice.common.config;

import cn.nyaaar.partridgemngservice.common.interceptor.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.providers.ActuatorProvider;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.Optional;

/**
 * @author yuegenhua
 * @Version $Id: InterceptorConfig.java, v 0.1 2022-27 10:33 yuegenhua Exp $$
 */
@Configuration
@Slf4j
public class InterceptorConfig extends SwaggerWebMvcConfigurer {
    @Autowired
    private UserInterceptor userInterceptor;

    public InterceptorConfig(SwaggerUiConfigParameters swaggerUiConfigParameters,
                             SwaggerIndexTransformer swaggerIndexTransformer,
                             Optional<ActuatorProvider> actuatorProvider) {
        super(swaggerUiConfigParameters, swaggerIndexTransformer, actuatorProvider);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
    }
}