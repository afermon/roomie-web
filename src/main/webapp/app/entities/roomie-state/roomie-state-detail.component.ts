import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomieState } from 'app/shared/model/roomie-state.model';

@Component({
    selector: 'jhi-roomie-state-detail',
    templateUrl: './roomie-state-detail.component.html'
})
export class RoomieStateDetailComponent implements OnInit {
    roomieState: IRoomieState;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomieState }) => {
            this.roomieState = roomieState;
        });
    }

    previousState() {
        window.history.back();
    }
}
