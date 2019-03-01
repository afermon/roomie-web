import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomEvent } from 'app/shared/model/room-event.model';

@Component({
    selector: 'jhi-room-event-detail',
    templateUrl: './room-event-detail.component.html'
})
export class RoomEventDetailComponent implements OnInit {
    roomEvent: IRoomEvent;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomEvent }) => {
            this.roomEvent = roomEvent;
        });
    }

    previousState() {
        window.history.back();
    }
}
