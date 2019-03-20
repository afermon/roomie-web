import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IRoomEvent } from 'app/shared/model/room-event.model';
import { RoomEventService } from './room-event.service';
import { IRoom } from 'app/shared/model/room.model';
import { RoomService } from 'app/entities/room';
import { IRoomie } from 'app/shared/model/roomie.model';
import { RoomieService } from 'app/entities/roomie';

@Component({
    selector: 'jhi-room-event-update',
    templateUrl: './room-event-update.component.html'
})
export class RoomEventUpdateComponent implements OnInit {
    roomEvent: IRoomEvent;
    isSaving: boolean;

    rooms: IRoom[];

    roomies: IRoomie[];
    startTime: string;
    endTime: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected roomEventService: RoomEventService,
        protected roomService: RoomService,
        protected roomieService: RoomieService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roomEvent }) => {
            this.roomEvent = roomEvent;
            this.startTime = this.roomEvent.startTime != null ? this.roomEvent.startTime.format(DATE_TIME_FORMAT) : null;
            this.endTime = this.roomEvent.endTime != null ? this.roomEvent.endTime.format(DATE_TIME_FORMAT) : null;
        });
        this.roomService.query().subscribe(
            (res: HttpResponse<IRoom[]>) => {
                this.rooms = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.roomieService.query().subscribe(
            (res: HttpResponse<IRoomie[]>) => {
                this.roomies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.roomEvent.startTime = this.startTime != null ? moment(this.startTime, DATE_TIME_FORMAT) : null;
        this.roomEvent.endTime = this.endTime != null ? moment(this.endTime, DATE_TIME_FORMAT) : null;
        if (this.roomEvent.id !== undefined) {
            this.subscribeToSaveResponse(this.roomEventService.update(this.roomEvent));
        } else {
            this.subscribeToSaveResponse(this.roomEventService.create(this.roomEvent));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomEvent>>) {
        result.subscribe((res: HttpResponse<IRoomEvent>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackRoomById(index: number, item: IRoom) {
        return item.id;
    }

    trackRoomieById(index: number, item: IRoomie) {
        return item.id;
    }
}
