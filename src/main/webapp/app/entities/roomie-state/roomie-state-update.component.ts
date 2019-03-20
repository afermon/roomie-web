import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { IRoomieState } from 'app/shared/model/roomie-state.model';
import { RoomieStateService } from './roomie-state.service';

@Component({
    selector: 'jhi-roomie-state-update',
    templateUrl: './roomie-state-update.component.html'
})
export class RoomieStateUpdateComponent implements OnInit {
    roomieState: IRoomieState;
    isSaving: boolean;
    suspendedDateDp: any;

    constructor(protected roomieStateService: RoomieStateService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roomieState }) => {
            this.roomieState = roomieState;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.roomieState.id !== undefined) {
            this.subscribeToSaveResponse(this.roomieStateService.update(this.roomieState));
        } else {
            this.subscribeToSaveResponse(this.roomieStateService.create(this.roomieState));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomieState>>) {
        result.subscribe((res: HttpResponse<IRoomieState>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
