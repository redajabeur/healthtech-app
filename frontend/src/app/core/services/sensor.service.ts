import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { environment } from '../../environments/environment';
import { Sensor, SensorReading } from '../models';

@Injectable({ providedIn: 'root' })
export class SensorService {

  private apiUrl = `${environment.apiUrl}/sensors`;
  private wsSubjects = new Map<string, WebSocketSubject<SensorReading>>();

  constructor(private http: HttpClient) {}

  getAll(): Observable<Sensor[]> {
    return this.http.get<Sensor[]>(this.apiUrl);
  }

  getByBuilding(buildingId: number): Observable<Sensor[]> {
    return this.http.get<Sensor[]>(`${this.apiUrl}/building/${buildingId}`);
  }

  getById(id: number): Observable<Sensor> {
    return this.http.get<Sensor>(`${this.apiUrl}/${id}`);
  }

  getReadings(sensorId: number, from?: string, to?: string): Observable<SensorReading[]> {
    let params = new HttpParams();
    if (from) params = params.set('from', from);
    if (to) params = params.set('to', to);
    return this.http.get<SensorReading[]>(`${this.apiUrl}/${sensorId}/readings`, { params });
  }

  getLatestReading(sensorId: number): Observable<SensorReading> {
    return this.http.get<SensorReading>(`${this.apiUrl}/${sensorId}/latest`);
  }

  getStatusSummary(): Observable<any> {
    return this.http.get(`${this.apiUrl}/status/summary`);
  }

  /** WebSocket temps réel pour un capteur */
  getRealtimeStream(sensorId: string): WebSocketSubject<SensorReading> {
    if (!this.wsSubjects.has(sensorId)) {
      const ws = webSocket<SensorReading>(`${environment.wsUrl}/topic/sensors/${sensorId}`);
      this.wsSubjects.set(sensorId, ws);
    }
    return this.wsSubjects.get(sensorId)!;
  }
}
