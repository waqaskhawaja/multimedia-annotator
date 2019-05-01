package pk.waqaskhawaja.ma.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(pk.waqaskhawaja.ma.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.DataType.class.getName(), jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.DataType.class.getName() + ".data", jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.InteractionType.class.getName(), jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.InteractionType.class.getName() + ".dataRecords", jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.Scenario.class.getName(), jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.Scenario.class.getName() + ".sessions", jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.Data.class.getName(), jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.Data.class.getName() + ".dataRecords", jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.Data.class.getName() + ".sessions", jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.DataRecord.class.getName(), jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.Analyst.class.getName(), jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.Analyst.class.getName() + ".sessions", jcacheConfiguration);
            cm.createCache(pk.waqaskhawaja.ma.domain.Session.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
