import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IRoom } from 'app/shared/model/room.model';
import { RoomService } from './room.service';
import { IAddress } from 'app/shared/model/address.model';
import { AddressService } from 'app/entities/address';
import { IRoomExpense } from 'app/shared/model/room-expense.model';
import { RoomExpenseService } from 'app/entities/room-expense';
import { IRoomie } from 'app/shared/model/roomie.model';
import { RoomieService } from 'app/entities/roomie';
import { IRoomFeature } from 'app/shared/model/room-feature.model';
import { RoomFeatureService } from 'app/entities/room-feature';
import { IRoomPicture } from 'app/shared/model/room-picture.model';
import { RoomPictureService } from 'app/entities/room-picture';

@Component({
    selector: 'jhi-room-update',
    templateUrl: './room-update.component.html'
})
export class RoomUpdateComponent implements OnInit {
    room: IRoom;
    isSaving: boolean;

    addresses: IAddress[];

    prices: IRoomExpense[];

    roomies: IRoomie[];

    roomfeatures: IRoomFeature[];

    deletedPics: IRoomPicture[];
    created: string;
    published: string;
    availableFromDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected roomService: RoomService,
        protected addressService: AddressService,
        protected roomExpenseService: RoomExpenseService,
        protected roomieService: RoomieService,
        protected roomFeatureService: RoomFeatureService,
        protected activatedRoute: ActivatedRoute,
        protected roomPictureService: RoomPictureService
    ) {
        this.deletedPics = [];
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ room }) => {
            this.room = room;
            this.created = this.room.created != null ? this.room.created.format(DATE_TIME_FORMAT) : null;
            this.published = this.room.published != null ? this.room.published.format(DATE_TIME_FORMAT) : null;
        });
        this.roomService.find(this.room.id).subscribe(res => {
            this.room = res.body;
            console.log(this.room);
        });
        this.addressService
            .query({ filter: 'room-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IAddress[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAddress[]>) => response.body)
            )
            .subscribe(
                (res: IAddress[]) => {
                    if (!this.room.addressId) {
                        this.addresses = res;
                    } else {
                        this.addressService
                            .find(this.room.addressId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IAddress>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IAddress>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IAddress) => (this.addresses = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.roomExpenseService
            .query({ filter: 'room-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IRoomExpense[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoomExpense[]>) => response.body)
            )
            .subscribe(
                (res: IRoomExpense[]) => {
                    if (!this.room.priceId) {
                        this.prices = res;
                    } else {
                        this.roomExpenseService
                            .find(this.room.priceId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IRoomExpense>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IRoomExpense>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IRoomExpense) => (this.prices = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.roomieService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IRoomie[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoomie[]>) => response.body)
            )
            .subscribe((res: IRoomie[]) => (this.roomies = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.roomFeatureService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IRoomFeature[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoomFeature[]>) => response.body)
            )
            .subscribe((res: IRoomFeature[]) => (this.roomfeatures = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.room.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.room.published = this.published != null ? moment(this.published, DATE_TIME_FORMAT) : null;
        if (this.room.id !== undefined) {
            this.subscribeToSaveResponse(this.roomService.update(this.room));
            for (const p of this.deletedPics) {
                this.roomPictureService.delete(p.id).subscribe();
            }
        } else {
            this.subscribeToSaveResponse(this.roomService.create(this.room));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoom>>) {
        result.subscribe((res: HttpResponse<IRoom>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAddressById(index: number, item: IAddress) {
        return item.id;
    }

    trackRoomExpenseById(index: number, item: IRoomExpense) {
        return item.id;
    }

    trackRoomieById(index: number, item: IRoomie) {
        return item.id;
    }

    trackRoomFeatureById(index: number, item: IRoomFeature) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }

    removePic(picture) {
        let i = 0;
        for (const pic of this.room.pictures) {
            if (pic.id === picture.id) {
                this.room.pictures.splice(i, 1);
                this.deletedPics.push(picture);
            }
            i++;
        }
    }
}
