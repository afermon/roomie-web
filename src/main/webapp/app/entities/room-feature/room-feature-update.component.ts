import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IRoomFeature } from 'app/shared/model/room-feature.model';
import { RoomFeatureService } from './room-feature.service';

@Component({
    selector: 'jhi-room-feature-update',
    templateUrl: './room-feature-update.component.html'
})
export class RoomFeatureUpdateComponent implements OnInit {
    roomFeature: IRoomFeature;
    isSaving: boolean;

    constructor(protected roomFeatureService: RoomFeatureService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roomFeature }) => {
            this.roomFeature = roomFeature;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.roomFeature.id !== undefined) {
            this.subscribeToSaveResponse(this.roomFeatureService.update(this.roomFeature));
        } else {
            this.subscribeToSaveResponse(this.roomFeatureService.create(this.roomFeature));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomFeature>>) {
        result.subscribe((res: HttpResponse<IRoomFeature>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
