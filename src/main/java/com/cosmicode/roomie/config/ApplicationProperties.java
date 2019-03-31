package com.cosmicode.roomie.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Roomie.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private int averageExchangeRateCrcToUsd;
    private String firebaseCloudMessagingKey;
    private String firebaseCloudMessagingUrl;

    public ApplicationProperties() {
    }

    public int getAverageExchangeRateCrcToUsd() {
        return averageExchangeRateCrcToUsd;
    }

    public void setAverageExchangeRateCrcToUsd(int averageExchangeRateCrcToUsd) {
        this.averageExchangeRateCrcToUsd = averageExchangeRateCrcToUsd;
    }

    public String getFirebaseCloudMessagingKey() {
        return firebaseCloudMessagingKey;
    }

    public void setFirebaseCloudMessagingKey(String firebaseCloudMessagingKey) {
        this.firebaseCloudMessagingKey = firebaseCloudMessagingKey;
    }

    public String getFirebaseCloudMessagingUrl() {
        return firebaseCloudMessagingUrl;
    }

    public void setFirebaseCloudMessagingUrl(String firebaseCloudMessagingUrl) {
        this.firebaseCloudMessagingUrl = firebaseCloudMessagingUrl;
    }
}
