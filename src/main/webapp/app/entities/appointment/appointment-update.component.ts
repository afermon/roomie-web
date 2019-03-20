import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IAppointment } from 'app/shared/model/appointment.model';
import { AppointmentService } from './appointment.service';
import { IRoomie } from 'app/shared/model/roomie.model';
import { RoomieService } from 'app/entities/roomie';
import { IRoom } from 'app/shared/model/room.model';
import { RoomService } from 'app/entities/room';

@Component({
    selector: 'jhi-appointment-update',
    templateUrl: './appointment-update.component.html'
})
export class AppointmentUpdateComponent implements OnInit {
    appointment: IAppointment;
    isSaving: boolean;

    roomies: IRoomie[];

    rooms: IRoom[];
    dateTime: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected appointmentService: AppointmentService,
        protected roomieService: RoomieService,
        protected roomService: RoomService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ appointment }) => {
            this.appointment = appointment;
            this.dateTime = this.appointment.dateTime != null ? this.appointment.dateTime.format(DATE_TIME_FORMAT) : null;
        });
        this.roomieService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IRoomie[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoomie[]>) => response.body)
            )
            .subscribe((res: IRoomie[]) => (this.roomies = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.roomService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IRoom[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoom[]>) => response.body)
            )
            .subscribe((res: IRoom[]) => (this.rooms = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.appointment.dateTime = this.dateTime != null ? moment(this.dateTime, DATE_TIME_FORMAT) : null;
        if (this.appointment.id !== undefined) {
            this.subscribeToSaveResponse(this.appointmentService.update(this.appointment));
        } else {
            this.subscribeToSaveResponse(this.appointmentService.create(this.appointment));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointment>>) {
        result.subscribe((res: HttpResponse<IAppointment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackRoomieById(index: number, item: IRoomie) {
        return item.id;
    }

    trackRoomById(index: number, item: IRoom) {
        return item.id;
    }
}
