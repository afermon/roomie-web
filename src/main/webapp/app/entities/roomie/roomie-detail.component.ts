import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomie } from 'app/shared/model/roomie.model';

@Component({
    selector: 'jhi-roomie-detail',
    templateUrl: './roomie-detail.component.html'
})
export class RoomieDetailComponent implements OnInit {
    roomie: IRoomie;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomie }) => {
            this.roomie = roomie;
        });
    }

    previousState() {
        window.history.back();
    }
}
