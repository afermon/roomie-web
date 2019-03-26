import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
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
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.roomieStateService
            .query({ filter: 'roomie-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IRoomieState[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoomieState[]>) => response.body)
            )
            .subscribe(
                (res: IRoomieState[]) => {
                    if (!this.roomie.stateId) {
                        this.states = res;
                    } else {
                        this.roomieStateService
                            .find(this.roomie.stateId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IRoomieState>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IRoomieState>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IRoomieState) => (this.states = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.addressService
            .query({ filter: 'roomie-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IAddress[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAddress[]>) => response.body)
            )
            .subscribe(
                (res: IAddress[]) => {
                    if (!this.roomie.addressId) {
                        this.addresses = res;
                    } else {
                        this.addressService
                            .find(this.roomie.addressId)
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
        this.userPreferencesService
            .query({ filter: 'roomie-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IUserPreferences[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUserPreferences[]>) => response.body)
            )
            .subscribe(
                (res: IUserPreferences[]) => {
                    if (!this.roomie.configurationId) {
                        this.configurations = res;
                    } else {
                        this.userPreferencesService
                            .find(this.roomie.configurationId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IUserPreferences>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IUserPreferences>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IUserPreferences) => (this.configurations = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
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
