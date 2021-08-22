package com.company.app.common.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 配置请求拦截器
 *
 * 在使用拦截器时，在配置拦截器的时候，由于在 Spring Boot 2.0 之前，我们都是直接继承 WebMvcConfigurerAdapter 类，
 * 然后重写 addInterceptors 方法来实现拦截器的配置。但是在 Spring Boot 2.0 之后，该方法已经被废弃了（当然，也可以继续用），
 * 取而代之的是 WebMvcConfigurationSupport 方法，如下
 */

@Configuration
public class RequestInterceptorConfig implements WebMvcConfigurer {

    //关键，将拦截器作为bean写入配置中,否则在拦截器中无法注入service
    @Bean
    public RequestInterceptor myInterceptor() {
        return new RequestInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor())
                .addPathPatterns("/**")
                /*.excludePathPatterns("/swagger-ui/*",//放行swagger的静态资源
                        "/swagger/**", "/v2/**",
                        "/swagger-resources/**",
                        "/webjars/**", "/static/**", "/error",
                        "/static/css/**","/static/js/**"
                )*/;
    }

    @Override
    public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(60 * 1000L);
        configurer.registerCallableInterceptors(timeoutInterceptor());
        configurer.setTaskExecutor(threadPoolTaskExecutor());
    }
    @Bean
    public TimeoutCallableProcessingInterceptor timeoutInterceptor() {
        return new TimeoutCallableProcessingInterceptor();
    }
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
        t.setCorePoolSize(10);
        t.setMaxPoolSize(100);
        t.setQueueCapacity(20);
        t.setThreadNamePrefix("PZJ-Thread-");
        return t;
    }

}
