import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomExpenseSplit } from 'app/shared/model/room-expense-split.model';

@Component({
    selector: 'jhi-room-expense-split-detail',
    templateUrl: './room-expense-split-detail.component.html'
})
export class RoomExpenseSplitDetailComponent implements OnInit {
    roomExpenseSplit: IRoomExpenseSplit;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomExpenseSplit }) => {
            this.roomExpenseSplit = roomExpenseSplit;
        });
    }

    previousState() {
        window.history.back();
    }
}
