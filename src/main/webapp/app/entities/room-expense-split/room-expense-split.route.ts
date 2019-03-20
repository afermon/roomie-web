import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RoomExpenseSplit } from 'app/shared/model/room-expense-split.model';
import { RoomExpenseSplitService } from './room-expense-split.service';
import { RoomExpenseSplitComponent } from './room-expense-split.component';
import { RoomExpenseSplitDetailComponent } from './room-expense-split-detail.component';
import { RoomExpenseSplitUpdateComponent } from './room-expense-split-update.component';
import { RoomExpenseSplitDeletePopupComponent } from './room-expense-split-delete-dialog.component';
import { IRoomExpenseSplit } from 'app/shared/model/room-expense-split.model';

@Injectable({ providedIn: 'root' })
export class RoomExpenseSplitResolve implements Resolve<IRoomExpenseSplit> {
    constructor(private service: RoomExpenseSplitService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRoomExpenseSplit> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RoomExpenseSplit>) => response.ok),
                map((roomExpenseSplit: HttpResponse<RoomExpenseSplit>) => roomExpenseSplit.body)
            );
        }
        return of(new RoomExpenseSplit());
    }
}

export const roomExpenseSplitRoute: Routes = [
    {
        path: '',
        component: RoomExpenseSplitComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.roomExpenseSplit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: RoomExpenseSplitDetailComponent,
        resolve: {
            roomExpenseSplit: RoomExpenseSplitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpenseSplit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: RoomExpenseSplitUpdateComponent,
        resolve: {
            roomExpenseSplit: RoomExpenseSplitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpenseSplit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: RoomExpenseSplitUpdateComponent,
        resolve: {
            roomExpenseSplit: RoomExpenseSplitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpenseSplit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roomExpenseSplitPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: RoomExpenseSplitDeletePopupComponent,
        resolve: {
            roomExpenseSplit: RoomExpenseSplitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpenseSplit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
