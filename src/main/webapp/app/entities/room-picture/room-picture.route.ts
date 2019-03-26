import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RoomPicture } from 'app/shared/model/room-picture.model';
import { RoomPictureService } from './room-picture.service';
import { RoomPictureComponent } from './room-picture.component';
import { RoomPictureDetailComponent } from './room-picture-detail.component';
import { RoomPictureUpdateComponent } from './room-picture-update.component';
import { RoomPictureDeletePopupComponent } from './room-picture-delete-dialog.component';
import { IRoomPicture } from 'app/shared/model/room-picture.model';

@Injectable({ providedIn: 'root' })
export class RoomPictureResolve implements Resolve<IRoomPicture> {
    constructor(private service: RoomPictureService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRoomPicture> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RoomPicture>) => response.ok),
                map((roomPicture: HttpResponse<RoomPicture>) => roomPicture.body)
            );
        }
        return of(new RoomPicture());
    }
}

export const roomPictureRoute: Routes = [
    {
        path: '',
        component: RoomPictureComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.roomPicture.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: RoomPictureDetailComponent,
        resolve: {
            roomPicture: RoomPictureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomPicture.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: RoomPictureUpdateComponent,
        resolve: {
            roomPicture: RoomPictureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomPicture.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: RoomPictureUpdateComponent,
        resolve: {
            roomPicture: RoomPictureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomPicture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roomPicturePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: RoomPictureDeletePopupComponent,
        resolve: {
            roomPicture: RoomPictureResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomPicture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
