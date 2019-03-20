import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IRoomTask } from 'app/shared/model/room-task.model';
import { RoomTaskService } from './room-task.service';
import { IRoom } from 'app/shared/model/room.model';
import { RoomService } from 'app/entities/room';

@Component({
    selector: 'jhi-room-task-update',
    templateUrl: './room-task-update.component.html'
})
export class RoomTaskUpdateComponent implements OnInit {
    roomTask: IRoomTask;
    isSaving: boolean;

    rooms: IRoom[];
    created: string;
    deadline: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected roomTaskService: RoomTaskService,
        protected roomService: RoomService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roomTask }) => {
            this.roomTask = roomTask;
            this.created = this.roomTask.created != null ? this.roomTask.created.format(DATE_TIME_FORMAT) : null;
            this.deadline = this.roomTask.deadline != null ? this.roomTask.deadline.format(DATE_TIME_FORMAT) : null;
        });
        this.roomService.query().subscribe(
            (res: HttpResponse<IRoom[]>) => {
                this.rooms = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.roomTask.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.roomTask.deadline = this.deadline != null ? moment(this.deadline, DATE_TIME_FORMAT) : null;
        if (this.roomTask.id !== undefined) {
            this.subscribeToSaveResponse(this.roomTaskService.update(this.roomTask));
        } else {
            this.subscribeToSaveResponse(this.roomTaskService.create(this.roomTask));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomTask>>) {
        result.subscribe((res: HttpResponse<IRoomTask>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
