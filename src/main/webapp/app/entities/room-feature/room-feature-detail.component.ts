import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomFeature } from 'app/shared/model/room-feature.model';

@Component({
    selector: 'jhi-room-feature-detail',
    templateUrl: './room-feature-detail.component.html'
})
export class RoomFeatureDetailComponent implements OnInit {
    roomFeature: IRoomFeature;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomFeature }) => {
            this.roomFeature = roomFeature;
        });
    }

    previousState() {
        window.history.back();
    }
}
