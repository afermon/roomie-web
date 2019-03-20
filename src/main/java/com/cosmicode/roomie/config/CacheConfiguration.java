package com.cosmicode.roomie.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

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
            cm.createCache(com.cosmicode.roomie.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Roomie.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Roomie.class.getName() + ".roomExpenseSplits", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Roomie.class.getName() + ".rooms", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Roomie.class.getName() + ".roomEvents", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Roomie.class.getName() + ".lifestyles", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.RoomieState.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Address.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.UserPreferences.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Room.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Room.class.getName() + ".roomExpenses", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Room.class.getName() + ".pictures", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Room.class.getName() + ".appointments", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Room.class.getName() + ".roomTasks", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Room.class.getName() + ".roomEvents", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Room.class.getName() + ".roomies", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Room.class.getName() + ".features", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.RoomPicture.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.RoomFeature.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.RoomExpense.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.RoomExpense.class.getName() + ".splits", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.RoomExpenseSplit.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.RoomExpenseSplit.class.getName() + ".records", jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.RoomExpenseSplitRecord.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Appointment.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.RoomTask.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.RoomEvent.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.UserReport.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Company.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Notification.class.getName(), jcacheConfiguration);
            cm.createCache(com.cosmicode.roomie.domain.Room.class.getName() + ".expenses", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
