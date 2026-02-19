package com.smartearthen.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartearthen.model.*;
import com.smartearthen.service.AlertService;
import com.smartearthen.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * Service MQTT — Écoute les capteurs IoT ESP32 et enregistre les lectures.
 * Chaque capteur publie sur : earthen/{buildingCode}/sensors/{sensorId}
 * Payload JSON : { "temp": 22.5, "humidity": 58.2, "co2": 420, "battery": 87 }
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MqttSubscriberService implements MqttCallback {

    private final SensorService sensorService;
    private final AlertService alertService;
    private final SimpMessagingTemplate wsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${mqtt.broker-url}")
    private String brokerUrl;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.topic.sensors}")
    private String sensorsTopic;

    private MqttClient mqttClient;

    @PostConstruct
    public void connect() {
        try {
            mqttClient = new MqttClient(brokerUrl, clientId + "-" + System.currentTimeMillis());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(false);
            options.setConnectionTimeout(10);
            mqttClient.setCallback(this);
            mqttClient.connect(options);
            mqttClient.subscribe(sensorsTopic, 1);
            log.info("✅ MQTT connecté à {} · Topic: {}", brokerUrl, sensorsTopic);
        } catch (MqttException e) {
            log.warn("⚠ MQTT non disponible (mode dégradé): {}", e.getMessage());
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            String sensorId = extractSensorId(topic);
            String payload = new String(message.getPayload());

            MqttPayload data = objectMapper.readValue(payload, MqttPayload.class);
            SensorReading reading = buildReading(sensorId, data, payload);

            sensorService.saveReading(sensorId, reading);
            alertService.checkThresholds(sensorId, reading);

            // Push WebSocket vers le frontend
            wsTemplate.convertAndSend("/topic/sensors/" + sensorId, reading);
            log.debug("📡 Données reçues — {} | T:{} HR:{}", sensorId, data.temp, data.humidity);

        } catch (Exception e) {
            log.error("Erreur traitement MQTT message: {}", e.getMessage());
        }
    }

    private SensorReading buildReading(String sensorId, MqttPayload data, String raw) {
        SensorReading r = new SensorReading();
        r.setRecordedAt(LocalDateTime.now());
        r.setTemperature(data.temp);
        r.setRelativeHumidity(data.humidity);
        r.setCo2Level(data.co2);
        r.setPressure(data.pressure);
        r.setMaterialMoisture(data.materialMoisture);
        r.setRssi(data.rssi);
        r.setRawPayload(raw);
        return r;
    }

    private String extractSensorId(String topic) {
        String[] parts = topic.split("/");
        return parts.length >= 4 ? parts[3] : "UNKNOWN";
    }

    @Override public void connectionLost(Throwable cause) {
        log.warn("🔴 MQTT connexion perdue: {}", cause.getMessage());
    }

    @Override public void deliveryComplete(IMqttDeliveryToken token) {}

    public record MqttPayload(
        Double temp, Double humidity, Double co2, Double pressure,
        Double materialMoisture, Integer rssi, Double battery
    ) {}
}
