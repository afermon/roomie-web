import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { IRoomie } from 'app/shared/model/roomie.model';
import { RoomieService } from './roomie.service';
import { IUser, UserService } from 'app/core';
import { IRoomieState } from 'app/shared/model/roomie-state.model';
import { RoomieStateService } from 'app/entities/roomie-state';
import { IAddress } from 'app/shared/model/address.model';
import { AddressService } from 'app/entities/address';
import { IUserPreferences } from 'app/shared/model/user-preferences.model';
import { UserPreferencesService } from 'app/entities/user-preferences';
import { IRoomFeature } from 'app/shared/model/room-feature.model';
import { RoomFeatureService } from 'app/entities/room-feature';

@Component({
    selector: 'jhi-roomie-update',
    templateUrl: './roomie-update.component.html'
})
export class RoomieUpdateComponent implements OnInit {
    roomie: IRoomie;
    isSaving: boolean;

    users: IUser[];

    states: IRoomieState[];

    addresses: IAddress[];

    configurations: IUserPreferences[];

    roomfeatures: IRoomFeature[];
    birthDateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected roomieService: RoomieService,
        protected userService: UserService,
        protected roomieStateService: RoomieStateService,
        protected addressService: AddressService,
        protected userPreferencesService: UserPreferencesService,
        protected roomFeatureService: RoomFeatureService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roomie }) => {
            this.roomie = roomie;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.roomieStateService.query({ filter: 'roomie-is-null' }).subscribe(
            (res: HttpResponse<IRoomieState[]>) => {
                if (!this.roomie.stateId) {
                    this.states = res.body;
                } else {
                    this.roomieStateService.find(this.roomie.stateId).subscribe(
                        (subRes: HttpResponse<IRoomieState>) => {
                            this.states = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.addressService.query({ filter: 'roomie-is-null' }).subscribe(
            (res: HttpResponse<IAddress[]>) => {
                if (!this.roomie.addressId) {
                    this.addresses = res.body;
                } else {
                    this.addressService.find(this.roomie.addressId).subscribe(
                        (subRes: HttpResponse<IAddress>) => {
                            this.addresses = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userPreferencesService.query({ filter: 'roomie-is-null' }).subscribe(
            (res: HttpResponse<IUserPreferences[]>) => {
                if (!this.roomie.configurationId) {
                    this.configurations = res.body;
                } else {
                    this.userPreferencesService.find(this.roomie.configurationId).subscribe(
                        (subRes: HttpResponse<IUserPreferences>) => {
                            this.configurations = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
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
        if (this.roomie.id !== undefined) {
            this.subscribeToSaveResponse(this.roomieService.update(this.roomie));
        } else {
            this.subscribeToSaveResponse(this.roomieService.create(this.roomie));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomie>>) {
        result.subscribe((res: HttpResponse<IRoomie>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackRoomieStateById(index: number, item: IRoomieState) {
        return item.id;
    }

    trackAddressById(index: number, item: IAddress) {
        return item.id;
    }

    trackUserPreferencesById(index: number, item: IUserPreferences) {
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
