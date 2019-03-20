import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IRoomExpense } from 'app/shared/model/room-expense.model';
import { RoomExpenseService } from './room-expense.service';
import { IRoom } from 'app/shared/model/room.model';
import { RoomService } from 'app/entities/room';

@Component({
    selector: 'jhi-room-expense-update',
    templateUrl: './room-expense-update.component.html'
})
export class RoomExpenseUpdateComponent implements OnInit {
    roomExpense: IRoomExpense;
    isSaving: boolean;

    rooms: IRoom[];
    startDateDp: any;
    finishDateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected roomExpenseService: RoomExpenseService,
        protected roomService: RoomService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roomExpense }) => {
            this.roomExpense = roomExpense;
        });
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
        if (this.roomExpense.id !== undefined) {
            this.subscribeToSaveResponse(this.roomExpenseService.update(this.roomExpense));
        } else {
            this.subscribeToSaveResponse(this.roomExpenseService.create(this.roomExpense));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomExpense>>) {
        result.subscribe((res: HttpResponse<IRoomExpense>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
