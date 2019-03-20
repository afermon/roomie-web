import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { IRoomExpenseSplitRecord } from 'app/shared/model/room-expense-split-record.model';
import { RoomExpenseSplitRecordService } from './room-expense-split-record.service';
import { IRoomExpenseSplit } from 'app/shared/model/room-expense-split.model';
import { RoomExpenseSplitService } from 'app/entities/room-expense-split';

@Component({
    selector: 'jhi-room-expense-split-record-update',
    templateUrl: './room-expense-split-record-update.component.html'
})
export class RoomExpenseSplitRecordUpdateComponent implements OnInit {
    roomExpenseSplitRecord: IRoomExpenseSplitRecord;
    isSaving: boolean;

    roomexpensesplits: IRoomExpenseSplit[];
    dateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected roomExpenseSplitRecordService: RoomExpenseSplitRecordService,
        protected roomExpenseSplitService: RoomExpenseSplitService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roomExpenseSplitRecord }) => {
            this.roomExpenseSplitRecord = roomExpenseSplitRecord;
        });
        this.roomExpenseSplitService.query().subscribe(
            (res: HttpResponse<IRoomExpenseSplit[]>) => {
                this.roomexpensesplits = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.roomExpenseSplitRecord.id !== undefined) {
            this.subscribeToSaveResponse(this.roomExpenseSplitRecordService.update(this.roomExpenseSplitRecord));
        } else {
            this.subscribeToSaveResponse(this.roomExpenseSplitRecordService.create(this.roomExpenseSplitRecord));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomExpenseSplitRecord>>) {
        result.subscribe(
            (res: HttpResponse<IRoomExpenseSplitRecord>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
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

    trackRoomExpenseSplitById(index: number, item: IRoomExpenseSplit) {
        return item.id;
    }
}
