import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoom } from 'app/shared/model/room.model';

@Component({
    selector: 'jhi-room-detail',
    templateUrl: './room-detail.component.html'
})
export class RoomDetailComponent implements OnInit {
    room: IRoom;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ room }) => {
            this.room = room;
        });
    }

    previousState() {
        window.history.back();
    }
}
