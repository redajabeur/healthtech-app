import { Component, OnInit } from '@angular/core';
import { AppointmentService, Appointment } from '../../../core/services/appointment.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  stats = {
    totalPatients: 1247,
    totalDoctors: 38,
    todayAppointments: 0,
    pendingAppointments: 0
  };

  recentAppointments: Appointment[] = [];
  isLoading = true;

  constructor(private appointmentService: AppointmentService) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.appointmentService.getAll().subscribe({
      next: (appointments) => {
        this.recentAppointments = appointments.slice(0, 5);
        this.stats.pendingAppointments = appointments
          .filter(a => a.status === 'PENDING').length;
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });

    this.appointmentService.getTodayCount().subscribe({
      next: (count) => this.stats.todayAppointments = count
    });
  }

  getStatusClass(status: string): string {
    const classes: Record<string, string> = {
      CONFIRMED: 'status-confirmed',
      PENDING: 'status-pending',
      CANCELLED: 'status-cancelled',
      COMPLETED: 'status-completed'
    };
    return classes[status] || '';
  }
}
