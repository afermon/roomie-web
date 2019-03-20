import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RoomExpense } from 'app/shared/model/room-expense.model';
import { RoomExpenseService } from './room-expense.service';
import { RoomExpenseComponent } from './room-expense.component';
import { RoomExpenseDetailComponent } from './room-expense-detail.component';
import { RoomExpenseUpdateComponent } from './room-expense-update.component';
import { RoomExpenseDeletePopupComponent } from './room-expense-delete-dialog.component';
import { IRoomExpense } from 'app/shared/model/room-expense.model';

@Injectable({ providedIn: 'root' })
export class RoomExpenseResolve implements Resolve<IRoomExpense> {
    constructor(private service: RoomExpenseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<RoomExpense> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RoomExpense>) => response.ok),
                map((roomExpense: HttpResponse<RoomExpense>) => roomExpense.body)
            );
        }
        return of(new RoomExpense());
    }
}

export const roomExpenseRoute: Routes = [
    {
        path: 'room-expense',
        component: RoomExpenseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.roomExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-expense/:id/view',
        component: RoomExpenseDetailComponent,
        resolve: {
            roomExpense: RoomExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-expense/new',
        component: RoomExpenseUpdateComponent,
        resolve: {
            roomExpense: RoomExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-expense/:id/edit',
        component: RoomExpenseUpdateComponent,
        resolve: {
            roomExpense: RoomExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roomExpensePopupRoute: Routes = [
    {
        path: 'room-expense/:id/delete',
        component: RoomExpenseDeletePopupComponent,
        resolve: {
            roomExpense: RoomExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomExpense.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
