import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RoomEvent } from 'app/shared/model/room-event.model';
import { RoomEventService } from './room-event.service';
import { RoomEventComponent } from './room-event.component';
import { RoomEventDetailComponent } from './room-event-detail.component';
import { RoomEventUpdateComponent } from './room-event-update.component';
import { RoomEventDeletePopupComponent } from './room-event-delete-dialog.component';
import { IRoomEvent } from 'app/shared/model/room-event.model';

@Injectable({ providedIn: 'root' })
export class RoomEventResolve implements Resolve<IRoomEvent> {
    constructor(private service: RoomEventService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRoomEvent> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RoomEvent>) => response.ok),
                map((roomEvent: HttpResponse<RoomEvent>) => roomEvent.body)
            );
        }
        return of(new RoomEvent());
    }
}

export const roomEventRoute: Routes = [
    {
        path: '',
        component: RoomEventComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.roomEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: RoomEventDetailComponent,
        resolve: {
            roomEvent: RoomEventResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: RoomEventUpdateComponent,
        resolve: {
            roomEvent: RoomEventResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: RoomEventUpdateComponent,
        resolve: {
            roomEvent: RoomEventResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roomEventPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: RoomEventDeletePopupComponent,
        resolve: {
            roomEvent: RoomEventResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
