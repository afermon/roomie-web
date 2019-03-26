import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomExpenseSplitRecord } from 'app/shared/model/room-expense-split-record.model';

@Component({
    selector: 'jhi-room-expense-split-record-detail',
    templateUrl: './room-expense-split-record-detail.component.html'
})
export class RoomExpenseSplitRecordDetailComponent implements OnInit {
    roomExpenseSplitRecord: IRoomExpenseSplitRecord;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomExpenseSplitRecord }) => {
            this.roomExpenseSplitRecord = roomExpenseSplitRecord;
        });
    }

    previousState() {
        window.history.back();
    }
}
