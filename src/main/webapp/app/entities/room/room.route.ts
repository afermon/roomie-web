import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Room } from 'app/shared/model/room.model';
import { RoomService } from './room.service';
import { RoomComponent } from './room.component';
import { RoomDetailComponent } from './room-detail.component';
import { RoomUpdateComponent } from './room-update.component';
import { RoomDeletePopupComponent } from './room-delete-dialog.component';
import { IRoom } from 'app/shared/model/room.model';

@Injectable({ providedIn: 'root' })
export class RoomResolve implements Resolve<IRoom> {
    constructor(private service: RoomService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Room> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Room>) => response.ok),
                map((room: HttpResponse<Room>) => room.body)
            );
        }
        return of(new Room());
    }
}

export const roomRoute: Routes = [
    {
        path: 'room',
        component: RoomComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.room.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room/:id/view',
        component: RoomDetailComponent,
        resolve: {
            room: RoomResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.room.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room/new',
        component: RoomUpdateComponent,
        resolve: {
            room: RoomResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.room.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room/:id/edit',
        component: RoomUpdateComponent,
        resolve: {
            room: RoomResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.room.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roomPopupRoute: Routes = [
    {
        path: 'room/:id/delete',
        component: RoomDeletePopupComponent,
        resolve: {
            room: RoomResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.room.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
