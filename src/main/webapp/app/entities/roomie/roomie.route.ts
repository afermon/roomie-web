import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Roomie } from 'app/shared/model/roomie.model';
import { RoomieService } from './roomie.service';
import { RoomieComponent } from './roomie.component';
import { RoomieDetailComponent } from './roomie-detail.component';
import { RoomieUpdateComponent } from './roomie-update.component';
import { RoomieDeletePopupComponent } from './roomie-delete-dialog.component';
import { IRoomie } from 'app/shared/model/roomie.model';

@Injectable({ providedIn: 'root' })
export class RoomieResolve implements Resolve<IRoomie> {
    constructor(private service: RoomieService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Roomie> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Roomie>) => response.ok),
                map((roomie: HttpResponse<Roomie>) => roomie.body)
            );
        }
        return of(new Roomie());
    }
}

export const roomieRoute: Routes = [
    {
        path: 'roomie',
        component: RoomieComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.roomie.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'roomie/:id/view',
        component: RoomieDetailComponent,
        resolve: {
            roomie: RoomieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomie.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'roomie/new',
        component: RoomieUpdateComponent,
        resolve: {
            roomie: RoomieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomie.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'roomie/:id/edit',
        component: RoomieUpdateComponent,
        resolve: {
            roomie: RoomieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomie.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roomiePopupRoute: Routes = [
    {
        path: 'roomie/:id/delete',
        component: RoomieDeletePopupComponent,
        resolve: {
            roomie: RoomieResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomie.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
