/**
 * Smart Earthen Buildings — Firmware ESP32
 * Capteur hygrothermique IoT → MQTT
 *
 * Matériel : ESP32 + DHT22 ou BME280
 * Bibliothèques : PubSubClient, DHT, ArduinoJson, WiFi
 */

#include <WiFi.h>
#include <PubSubClient.h>
#include <DHT.h>
#include <ArduinoJson.h>

// ===== CONFIGURATION =====
#define SENSOR_ID     "SEB-01"
#define BUILDING_CODE "batA"
#define DHT_PIN       4
#define DHT_TYPE      DHT22
#define SEND_INTERVAL 30000  // 30 secondes

const char* WIFI_SSID     = "VotreWiFi";
const char* WIFI_PASSWORD = "VotreMotDePasse";
const char* MQTT_BROKER   = "192.168.1.100";  // IP du serveur
const int   MQTT_PORT     = 1883;

// Topic MQTT : earthen/{building}/sensors/{sensorId}
String MQTT_TOPIC = "earthen/" + String(BUILDING_CODE) + "/sensors/" + String(SENSOR_ID);

// ===== OBJETS =====
WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);
DHT dht(DHT_PIN, DHT_TYPE);

unsigned long lastSend = 0;

void setup() {
  Serial.begin(115200);
  dht.begin();

  connectWiFi();
  mqttClient.setServer(MQTT_BROKER, MQTT_PORT);
  connectMQTT();

  Serial.println("✅ Smart Earthen Sensor — Online");
  Serial.println("📡 Capteur: " + String(SENSOR_ID));
  Serial.println("🏛️  Bâtiment: " + String(BUILDING_CODE));
}

void loop() {
  if (!mqttClient.connected()) connectMQTT();
  mqttClient.loop();

  if (millis() - lastSend >= SEND_INTERVAL) {
    sendReading();
    lastSend = millis();
  }
}

void sendReading() {
  float temp = dht.readTemperature();
  float humidity = dht.readHumidity();

  if (isnan(temp) || isnan(humidity)) {
    Serial.println("⚠ Erreur lecture DHT22");
    return;
  }

  // Calcul humidité absolue
  float absHumidity = (6.112 * exp(17.67 * temp / (temp + 243.5)) * humidity * 2.1674) / (273.15 + temp);

  // Calcul point de rosée
  float dewPoint = temp - ((100 - humidity) / 5.0);

  // Batterie (analogique)
  int rawBatt = analogRead(34);
  float battery = (rawBatt / 4095.0) * 100.0;

  // Construire JSON
  StaticJsonDocument<256> doc;
  doc["sensorId"]      = SENSOR_ID;
  doc["building"]      = BUILDING_CODE;
  doc["temp"]          = round(temp * 10) / 10.0;
  doc["humidity"]      = round(humidity * 10) / 10.0;
  doc["absHumidity"]   = round(absHumidity * 100) / 100.0;
  doc["dewPoint"]      = round(dewPoint * 10) / 10.0;
  doc["battery"]       = round(battery);
  doc["rssi"]          = WiFi.RSSI();
  doc["timestamp"]     = millis();

  char payload[256];
  serializeJson(doc, payload);

  if (mqttClient.publish(MQTT_TOPIC.c_str(), payload)) {
    Serial.printf("📤 T:%.1f°C HR:%.1f%% → %s\n", temp, humidity, MQTT_TOPIC.c_str());
  } else {
    Serial.println("❌ Échec publication MQTT");
  }
}

void connectWiFi() {
  Serial.print("🔌 Connexion WiFi");
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500); Serial.print(".");
  }
  Serial.println("\n✅ WiFi connecté — IP: " + WiFi.localIP().toString());
}

void connectMQTT() {
  String clientId = "ESP32-" + String(SENSOR_ID);
  while (!mqttClient.connected()) {
    Serial.print("🔌 Connexion MQTT...");
    if (mqttClient.connect(clientId.c_str())) {
      Serial.println("✅ MQTT connecté");
    } else {
      Serial.print("❌ Erreur: "); Serial.println(mqttClient.state());
      delay(3000);
    }
  }
}
