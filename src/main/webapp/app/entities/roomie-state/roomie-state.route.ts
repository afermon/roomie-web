import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RoomieState } from 'app/shared/model/roomie-state.model';
import { RoomieStateService } from './roomie-state.service';
import { RoomieStateComponent } from './roomie-state.component';
import { RoomieStateDetailComponent } from './roomie-state-detail.component';
import { RoomieStateUpdateComponent } from './roomie-state-update.component';
import { RoomieStateDeletePopupComponent } from './roomie-state-delete-dialog.component';
import { IRoomieState } from 'app/shared/model/roomie-state.model';

@Injectable({ providedIn: 'root' })
export class RoomieStateResolve implements Resolve<IRoomieState> {
    constructor(private service: RoomieStateService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRoomieState> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RoomieState>) => response.ok),
                map((roomieState: HttpResponse<RoomieState>) => roomieState.body)
            );
        }
        return of(new RoomieState());
    }
}

export const roomieStateRoute: Routes = [
    {
        path: '',
        component: RoomieStateComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.roomieState.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: RoomieStateDetailComponent,
        resolve: {
            roomieState: RoomieStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomieState.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: RoomieStateUpdateComponent,
        resolve: {
            roomieState: RoomieStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomieState.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: RoomieStateUpdateComponent,
        resolve: {
            roomieState: RoomieStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomieState.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roomieStatePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: RoomieStateDeletePopupComponent,
        resolve: {
            roomieState: RoomieStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomieState.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
