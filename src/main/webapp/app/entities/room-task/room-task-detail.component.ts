import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomTask } from 'app/shared/model/room-task.model';

@Component({
    selector: 'jhi-room-task-detail',
    templateUrl: './room-task-detail.component.html'
})
export class RoomTaskDetailComponent implements OnInit {
    roomTask: IRoomTask;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomTask }) => {
            this.roomTask = roomTask;
        });
    }

    previousState() {
        window.history.back();
    }
}
