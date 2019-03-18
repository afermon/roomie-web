import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
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
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ room }) => {
            this.room = room;
            this.created = this.room.created != null ? this.room.created.format(DATE_TIME_FORMAT) : null;
            this.published = this.room.published != null ? this.room.published.format(DATE_TIME_FORMAT) : null;
        });
        this.addressService.query({ filter: 'room-is-null' }).subscribe(
            (res: HttpResponse<IAddress[]>) => {
                if (!this.room.addressId) {
                    this.addresses = res.body;
                } else {
                    this.addressService.find(this.room.addressId).subscribe(
                        (subRes: HttpResponse<IAddress>) => {
                            this.addresses = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.roomExpenseService.query({ filter: 'room-is-null' }).subscribe(
            (res: HttpResponse<IRoomExpense[]>) => {
                if (!this.room.priceId) {
                    this.prices = res.body;
                } else {
                    this.roomExpenseService.find(this.room.priceId).subscribe(
                        (subRes: HttpResponse<IRoomExpense>) => {
                            this.prices = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.roomieService.query().subscribe(
            (res: HttpResponse<IRoomie[]>) => {
                this.roomies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.roomFeatureService.query().subscribe(
            (res: HttpResponse<IRoomFeature[]>) => {
                this.roomfeatures = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
}
