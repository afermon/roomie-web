import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IRoomPicture } from 'app/shared/model/room-picture.model';
import { RoomPictureService } from './room-picture.service';
import { IRoom } from 'app/shared/model/room.model';
import { RoomService } from 'app/entities/room';

@Component({
    selector: 'jhi-room-picture-update',
    templateUrl: './room-picture-update.component.html'
})
export class RoomPictureUpdateComponent implements OnInit {
    roomPicture: IRoomPicture;
    isSaving: boolean;

    rooms: IRoom[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected roomPictureService: RoomPictureService,
        protected roomService: RoomService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roomPicture }) => {
            this.roomPicture = roomPicture;
        });
        this.roomService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IRoom[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoom[]>) => response.body)
            )
            .subscribe((res: IRoom[]) => (this.rooms = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.roomPicture.id !== undefined) {
            this.subscribeToSaveResponse(this.roomPictureService.update(this.roomPicture));
        } else {
            this.subscribeToSaveResponse(this.roomPictureService.create(this.roomPicture));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomPicture>>) {
        result.subscribe((res: HttpResponse<IRoomPicture>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackRoomById(index: number, item: IRoom) {
        return item.id;
    }
}
