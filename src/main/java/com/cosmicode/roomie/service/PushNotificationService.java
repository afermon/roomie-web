package com.cosmicode.roomie.service;

import com.cosmicode.roomie.config.ApplicationProperties;
import com.cosmicode.roomie.domain.Notification;
import com.cosmicode.roomie.domain.enumeration.NotificationType;
import com.cosmicode.roomie.service.dto.RoomieDTO;
import com.cosmicode.roomie.service.dto.UserPreferencesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PushNotificationService {

    private final Logger log = LoggerFactory.getLogger(PushNotificationService.class);
    private final ApplicationProperties applicationProperties;
    private final RoomieService roomieService;
    private final UserPreferencesService userPreferencesService;

    public PushNotificationService(ApplicationProperties applicationProperties, RoomieService roomieService, UserPreferencesService userPreferencesService) {
        this.applicationProperties = applicationProperties;
        this.roomieService = roomieService;
        this.userPreferencesService = userPreferencesService;
    }

    public void send(Notification notification){
        try {

            Optional<RoomieDTO> roomieOptional = roomieService.findOne(notification.getRecipient().getId());
            if (!roomieOptional.isPresent()) throw new NoSuchFieldException();
            RoomieDTO roomie = roomieOptional.get();

            Optional<UserPreferencesDTO> preferencesOptional = userPreferencesService.findOne(notification.getRecipient().getId());
            if (!preferencesOptional.isPresent()) throw new NoSuchFieldException();
            UserPreferencesDTO preferences = preferencesOptional.get();

            if( (notification.getType().equals(NotificationType.APPOINTMENT) && !preferences.isAppointmentsNotifications()) ||
                (notification.getType().equals(NotificationType.TODO) && !preferences.isTodoListNotifications()) ||
                (notification.getType().equals(NotificationType.EXPENSE) && !preferences.isPaymentsNotifications()) ||
                (notification.getType().equals(NotificationType.EVENT) && !preferences.isCalendarNotifications())
                ){
                log.info("Notification disabled for {} for this user.", notification.getType().name());
                return;
            }

            JSONObject notificationJSON = new JSONObject();
            JSONObject dataJSON = new JSONObject();
            JSONObject requestJSON = new JSONObject();

            notificationJSON.put("title", notification.getTitle());
            notificationJSON.put("body", notification.getBody());

            dataJSON.put("id", notification.getId());
            dataJSON.put("type", notification.getType());
            dataJSON.put("roomie", notification.getRecipient().getId());
            dataJSON.put("entity", notification.getEntityId().toString());
            dataJSON.put("created", notification.getCreated().toString());

            requestJSON.put("to", roomie.getMobileDeviceID());
            requestJSON.put("notification", notificationJSON);
            requestJSON.put("data", dataJSON);

            log.debug("Notification request {}", requestJSON.toString() );

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "key=" + applicationProperties.getFirebaseCloudMessagingKey());
            httpHeaders.set("Content-Type", "application/json");
            HttpEntity<String> httpEntity = new HttpEntity<>(requestJSON.toString(), httpHeaders);
            String response = restTemplate.postForObject(applicationProperties.getFirebaseCloudMessagingUrl(),httpEntity,String.class);

            log.debug("Response from firebase: {}", response);
        } catch (JSONException e) {
            log.error("Error in request: {}", e.toString());
        } catch (NoSuchFieldException | NullPointerException e) {
            log.error("Invalid notification: {}", e.toString());
        }
    }

}
