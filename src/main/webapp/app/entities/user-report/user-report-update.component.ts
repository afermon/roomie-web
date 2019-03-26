import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IUserReport } from 'app/shared/model/user-report.model';
import { UserReportService } from './user-report.service';
import { IRoomie } from 'app/shared/model/roomie.model';
import { RoomieService } from 'app/entities/roomie';
import { IRoom } from 'app/shared/model/room.model';
import { RoomService } from 'app/entities/room';

@Component({
    selector: 'jhi-user-report-update',
    templateUrl: './user-report-update.component.html'
})
export class UserReportUpdateComponent implements OnInit {
    userReport: IUserReport;
    isSaving: boolean;

    roomies: IRoomie[];

    rooms: IRoom[];
    dateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected userReportService: UserReportService,
        protected roomieService: RoomieService,
        protected roomService: RoomService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userReport }) => {
            this.userReport = userReport;
        });
        this.roomieService
            .query({ filter: 'userreport-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IRoomie[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoomie[]>) => response.body)
            )
            .subscribe(
                (res: IRoomie[]) => {
                    if (!this.userReport.roomieId) {
                        this.roomies = res;
                    } else {
                        this.roomieService
                            .find(this.userReport.roomieId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IRoomie>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IRoomie>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IRoomie) => (this.roomies = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.roomService
            .query({ filter: 'userreport-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IRoom[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoom[]>) => response.body)
            )
            .subscribe(
                (res: IRoom[]) => {
                    if (!this.userReport.roomId) {
                        this.rooms = res;
                    } else {
                        this.roomService
                            .find(this.userReport.roomId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IRoom>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IRoom>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IRoom) => (this.rooms = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.userReport.id !== undefined) {
            this.subscribeToSaveResponse(this.userReportService.update(this.userReport));
        } else {
            this.subscribeToSaveResponse(this.userReportService.create(this.userReport));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserReport>>) {
        result.subscribe((res: HttpResponse<IUserReport>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackRoomieById(index: number, item: IRoomie) {
        return item.id;
    }

    trackRoomById(index: number, item: IRoom) {
        return item.id;
    }
}
