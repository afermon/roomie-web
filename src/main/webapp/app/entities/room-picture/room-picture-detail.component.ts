import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomPicture } from 'app/shared/model/room-picture.model';

@Component({
    selector: 'jhi-room-picture-detail',
    templateUrl: './room-picture-detail.component.html'
})
export class RoomPictureDetailComponent implements OnInit {
    roomPicture: IRoomPicture;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomPicture }) => {
            this.roomPicture = roomPicture;
        });
    }

    previousState() {
        window.history.back();
    }
}
