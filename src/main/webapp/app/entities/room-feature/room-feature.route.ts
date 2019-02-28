import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RoomFeature } from 'app/shared/model/room-feature.model';
import { RoomFeatureService } from './room-feature.service';
import { RoomFeatureComponent } from './room-feature.component';
import { RoomFeatureDetailComponent } from './room-feature-detail.component';
import { RoomFeatureUpdateComponent } from './room-feature-update.component';
import { RoomFeatureDeletePopupComponent } from './room-feature-delete-dialog.component';
import { IRoomFeature } from 'app/shared/model/room-feature.model';

@Injectable({ providedIn: 'root' })
export class RoomFeatureResolve implements Resolve<IRoomFeature> {
    constructor(private service: RoomFeatureService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<RoomFeature> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RoomFeature>) => response.ok),
                map((roomFeature: HttpResponse<RoomFeature>) => roomFeature.body)
            );
        }
        return of(new RoomFeature());
    }
}

export const roomFeatureRoute: Routes = [
    {
        path: 'room-feature',
        component: RoomFeatureComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.roomFeature.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-feature/:id/view',
        component: RoomFeatureDetailComponent,
        resolve: {
            roomFeature: RoomFeatureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomFeature.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-feature/new',
        component: RoomFeatureUpdateComponent,
        resolve: {
            roomFeature: RoomFeatureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomFeature.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-feature/:id/edit',
        component: RoomFeatureUpdateComponent,
        resolve: {
            roomFeature: RoomFeatureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomFeature.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roomFeaturePopupRoute: Routes = [
    {
        path: 'room-feature/:id/delete',
        component: RoomFeatureDeletePopupComponent,
        resolve: {
            roomFeature: RoomFeatureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomFeature.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
