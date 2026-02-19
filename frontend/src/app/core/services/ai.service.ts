import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ComfortScore, EnergyForecast } from '../models';

@Injectable({ providedIn: 'root' })
export class AiService {

  private apiUrl = `${environment.apiUrl}/ai`;

  constructor(private http: HttpClient) {}

  getComfortScore(buildingId: number): Observable<ComfortScore> {
    return this.http.get<ComfortScore>(`${this.apiUrl}/comfort-score/${buildingId}`);
  }

  getPredictions(buildingId: number, horizonHours = 48): Observable<any> {
    return this.http.get(`${this.apiUrl}/predictions/${buildingId}`, {
      params: { horizonHours: horizonHours.toString() }
    });
  }

  getAnomalies(buildingId?: number): Observable<any[]> {
    const params = buildingId ? { buildingId: buildingId.toString() } : {};
    return this.http.get<any[]>(`${this.apiUrl}/anomalies`, { params });
  }

  getEnergyForecast(buildingId: number): Observable<EnergyForecast> {
    return this.http.get<EnergyForecast>(`${this.apiUrl}/energy-forecast/${buildingId}`);
  }

  getMaterialStatus(sensorId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/material-status/${sensorId}`);
  }
}
