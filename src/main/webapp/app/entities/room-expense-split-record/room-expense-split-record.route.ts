import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RoomExpenseSplitRecord } from 'app/shared/model/room-expense-split-record.model';
import { RoomExpenseSplitRecordService } from './room-expense-split-record.service';
import { RoomExpenseSplitRecordComponent } from './room-expense-split-record.component';
import { RoomExpenseSplitRecordDetailComponent } from './room-expense-split-record-detail.component';
import { RoomExpenseSplitRecordUpdateComponent } from './room-expense-split-record-update.component';
import { RoomExpenseSplitRecordDeletePopupComponent } from './room-expense-split-record-delete-dialog.component';
import { IRoomExpenseSplitRecord } from 'app/shared/model/room-expense-split-record.model';

@Injectable({ providedIn: 'root' })
export class RoomExpenseSplitRecordResolve implements Resolve<IRoomExpenseSplitRecord> {
    constructor(private service: RoomExpenseSplitRecordService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<RoomExpenseSplitRecord> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RoomExpenseSplitRecord>) => response.ok),
                map((roomExpenseSplitRecord: HttpResponse<RoomExpenseSplitRecord>) => roomExpenseSplitRecord.body)
            );
        }
        return of(new RoomExpenseSplitRecord());
    }
}

export const roomExpenseSplitRecordRoute: Routes = [
    {
        path: 'room-expense-split-record',
        component: RoomExpenseSplitRecordComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.roomExpenseSplitRecord.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-expense-split-record/:id/view',
        component: RoomExpenseSplitRecordDetailComponent,
        resolve: {
            roomExpenseSplitRecord: RoomExpenseSplitRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpenseSplitRecord.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-expense-split-record/new',
        component: RoomExpenseSplitRecordUpdateComponent,
        resolve: {
            roomExpenseSplitRecord: RoomExpenseSplitRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpenseSplitRecord.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-expense-split-record/:id/edit',
        component: RoomExpenseSplitRecordUpdateComponent,
        resolve: {
            roomExpenseSplitRecord: RoomExpenseSplitRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpenseSplitRecord.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roomExpenseSplitRecordPopupRoute: Routes = [
    {
        path: 'room-expense-split-record/:id/delete',
        component: RoomExpenseSplitRecordDeletePopupComponent,
        resolve: {
            roomExpenseSplitRecord: RoomExpenseSplitRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpenseSplitRecord.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
