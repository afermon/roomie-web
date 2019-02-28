import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RoomTask } from 'app/shared/model/room-task.model';
import { RoomTaskService } from './room-task.service';
import { RoomTaskComponent } from './room-task.component';
import { RoomTaskDetailComponent } from './room-task-detail.component';
import { RoomTaskUpdateComponent } from './room-task-update.component';
import { RoomTaskDeletePopupComponent } from './room-task-delete-dialog.component';
import { IRoomTask } from 'app/shared/model/room-task.model';

@Injectable({ providedIn: 'root' })
export class RoomTaskResolve implements Resolve<IRoomTask> {
    constructor(private service: RoomTaskService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<RoomTask> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RoomTask>) => response.ok),
                map((roomTask: HttpResponse<RoomTask>) => roomTask.body)
            );
        }
        return of(new RoomTask());
    }
}

export const roomTaskRoute: Routes = [
    {
        path: 'room-task',
        component: RoomTaskComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.roomTask.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-task/:id/view',
        component: RoomTaskDetailComponent,
        resolve: {
            roomTask: RoomTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomTask.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-task/new',
        component: RoomTaskUpdateComponent,
        resolve: {
            roomTask: RoomTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomTask.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'room-task/:id/edit',
        component: RoomTaskUpdateComponent,
        resolve: {
            roomTask: RoomTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomTask.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roomTaskPopupRoute: Routes = [
    {
        path: 'room-task/:id/delete',
        component: RoomTaskDeletePopupComponent,
        resolve: {
            roomTask: RoomTaskResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.roomTask.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
