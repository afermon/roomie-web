import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IRoomExpenseSplit } from 'app/shared/model/room-expense-split.model';
import { RoomExpenseSplitService } from './room-expense-split.service';
import { IRoomExpense } from 'app/shared/model/room-expense.model';
import { RoomExpenseService } from 'app/entities/room-expense';
import { IRoomie } from 'app/shared/model/roomie.model';
import { RoomieService } from 'app/entities/roomie';

@Component({
    selector: 'jhi-room-expense-split-update',
    templateUrl: './room-expense-split-update.component.html'
})
export class RoomExpenseSplitUpdateComponent implements OnInit {
    roomExpenseSplit: IRoomExpenseSplit;
    isSaving: boolean;

    roomexpenses: IRoomExpense[];

    roomies: IRoomie[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected roomExpenseSplitService: RoomExpenseSplitService,
        protected roomExpenseService: RoomExpenseService,
        protected roomieService: RoomieService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roomExpenseSplit }) => {
            this.roomExpenseSplit = roomExpenseSplit;
        });
        this.roomExpenseService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IRoomExpense[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoomExpense[]>) => response.body)
            )
            .subscribe((res: IRoomExpense[]) => (this.roomexpenses = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.roomieService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IRoomie[]>) => mayBeOk.ok),
                map((response: HttpResponse<IRoomie[]>) => response.body)
            )
            .subscribe((res: IRoomie[]) => (this.roomies = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.roomExpenseSplit.id !== undefined) {
            this.subscribeToSaveResponse(this.roomExpenseSplitService.update(this.roomExpenseSplit));
        } else {
            this.subscribeToSaveResponse(this.roomExpenseSplitService.create(this.roomExpenseSplit));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomExpenseSplit>>) {
        result.subscribe((res: HttpResponse<IRoomExpenseSplit>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackRoomExpenseById(index: number, item: IRoomExpense) {
        return item.id;
    }

    trackRoomieById(index: number, item: IRoomie) {
        return item.id;
    }
}
