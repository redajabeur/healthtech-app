// building.model.ts
export interface Building {
  id?: number;
  name: string;
  code: string;
  material: 'PISE' | 'ADOBE' | 'BTC' | 'COB' | 'TORCHIS' | 'BAUGE';
  location: string;
  latitude?: number;
  longitude?: number;
  wallThicknessCm?: number;
  constructionYear?: number;
  floorAreaM2?: number;
  active: boolean;
}

// sensor.model.ts
export interface Sensor {
  id?: number;
  sensorId: string;
  type: 'DHT22' | 'BME280' | 'SHT31' | 'BME680' | 'DS18B20';
  location: string;
  zone: string;
  mqttTopic?: string;
  status: 'ONLINE' | 'OFFLINE' | 'LOW_BATTERY' | 'FAULTY';
  batteryLevel?: number;
  lastSeen?: string;
  buildingId: number;
  buildingName?: string;
  latestReading?: SensorReading;
}

// sensor-reading.model.ts
export interface SensorReading {
  id?: number;
  sensorId: string;
  recordedAt: string;
  temperature?: number;
  relativeHumidity?: number;
  co2Level?: number;
  pressure?: number;
  materialMoisture?: number;
  rssi?: number;
}

// ai.model.ts
export interface ComfortScore {
  buildingId: number;
  overallScore: number;
  temperatureScore: number;
  humidityScore: number;
  co2Score: number;
  comfortLevel: string;
  recommendation: string;
}

export interface EnergyForecast {
  buildingId: number;
  predictedConsumptionKwh: number;
  savingsKwh: number;
  savingsPercent: number;
  savingsCostMAD: number;
  co2ReductionKg: number;
  recommendation: string;
}

// alert.model.ts
export interface Alert {
  id?: number;
  buildingId: number;
  sensorId?: number;
  type: string;
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  title: string;
  description: string;
  recommendation?: string;
  triggerValue?: number;
  thresholdValue?: number;
  triggeredAt: string;
  active: boolean;
  aiGenerated: boolean;
}
