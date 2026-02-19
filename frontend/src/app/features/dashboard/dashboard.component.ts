import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject, interval, takeUntil, switchMap } from 'rxjs';
import { SensorService } from '../../../core/services/sensor.service';
import { AiService } from '../../../core/services/ai.service';
import { Sensor, Alert, ComfortScore, EnergyForecast } from '../../../core/models';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {

  private destroy$ = new Subject<void>();

  // État global
  selectedBuildingId = 1;
  isLoading = true;

  // KPIs
  avgTemperature = 0;
  avgHumidity = 0;
  energySavings = 0;
  activeSensors = 0;
  totalSensors = 0;

  // Données
  sensors: Sensor[] = [];
  activeAlerts: Alert[] = [];
  comfortScore: ComfortScore | null = null;
  energyForecast: EnergyForecast | null = null;

  // Graphiques
  temperatureChartData: any[] = [];
  humidityChartData: any[] = [];

  // Bâtiments
  buildings = [
    { id: 1, name: 'Bâtiment A — Pisé', material: 'PISE' },
    { id: 2, name: 'Bâtiment B — Adobe', material: 'ADOBE' },
    { id: 3, name: 'Bâtiment C — BTC', material: 'BTC' },
    { id: 4, name: 'Prototype D — Cob', material: 'COB' },
  ];

  // Table columns
  displayedColumns = ['sensorId', 'location', 'temperature', 'humidity', 'status', 'battery'];

  constructor(
    private sensorService: SensorService,
    private aiService: AiService
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
    // Actualisation toutes les 30 secondes
    interval(30000).pipe(takeUntil(this.destroy$))
      .subscribe(() => this.loadDashboard());
  }

  loadDashboard(): void {
    const bId = this.selectedBuildingId;

    this.sensorService.getByBuilding(bId).subscribe(sensors => {
      this.sensors = sensors;
      this.activeSensors = sensors.filter(s => s.status === 'ONLINE').length;
      this.totalSensors = sensors.length;

      const readings = sensors.filter(s => s.latestReading);
      if (readings.length > 0) {
        this.avgTemperature = readings.reduce((s, r) => s + (r.latestReading?.temperature || 0), 0) / readings.length;
        this.avgHumidity = readings.reduce((s, r) => s + (r.latestReading?.relativeHumidity || 0), 0) / readings.length;
      }
      this.isLoading = false;
    });

    this.aiService.getComfortScore(bId).subscribe(score => this.comfortScore = score);
    this.aiService.getEnergyForecast(bId).subscribe(forecast => {
      this.energyForecast = forecast;
      this.energySavings = forecast.savingsPercent;
    });
  }

  selectBuilding(buildingId: number): void {
    this.selectedBuildingId = buildingId;
    this.isLoading = true;
    this.loadDashboard();
  }

  getSeverityClass(severity: string): string {
    const classes: Record<string, string> = {
      CRITICAL: 'severity-critical',
      HIGH: 'severity-high',
      MEDIUM: 'severity-medium',
      LOW: 'severity-low'
    };
    return classes[severity] || '';
  }

  getStatusIcon(status: string): string {
    const icons: Record<string, string> = {
      ONLINE: '🟢', OFFLINE: '🔴', LOW_BATTERY: '🟡', FAULTY: '🔴'
    };
    return icons[status] || '⚪';
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
