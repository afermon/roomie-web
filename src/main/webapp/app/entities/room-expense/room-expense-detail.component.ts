import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomExpense } from 'app/shared/model/room-expense.model';

@Component({
    selector: 'jhi-room-expense-detail',
    templateUrl: './room-expense-detail.component.html'
})
export class RoomExpenseDetailComponent implements OnInit {
    roomExpense: IRoomExpense;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomExpense }) => {
            this.roomExpense = roomExpense;
        });
    }

    previousState() {
        window.history.back();
    }
}
