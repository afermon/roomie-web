import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UserReport } from 'app/shared/model/user-report.model';
import { UserReportService } from './user-report.service';
import { UserReportComponent } from './user-report.component';
import { UserReportDetailComponent } from './user-report-detail.component';
import { UserReportUpdateComponent } from './user-report-update.component';
import { UserReportDeletePopupComponent } from './user-report-delete-dialog.component';
import { IUserReport } from 'app/shared/model/user-report.model';

@Injectable({ providedIn: 'root' })
export class UserReportResolve implements Resolve<IUserReport> {
    constructor(private service: UserReportService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<UserReport> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<UserReport>) => response.ok),
                map((userReport: HttpResponse<UserReport>) => userReport.body)
            );
        }
        return of(new UserReport());
    }
}

export const userReportRoute: Routes = [
    {
        path: 'user-report',
        component: UserReportComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'roomieApp.userReport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-report/:id/view',
        component: UserReportDetailComponent,
        resolve: {
            userReport: UserReportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.userReport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-report/new',
        component: UserReportUpdateComponent,
        resolve: {
            userReport: UserReportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.userReport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-report/:id/edit',
        component: UserReportUpdateComponent,
        resolve: {
            userReport: UserReportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.userReport.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userReportPopupRoute: Routes = [
    {
        path: 'user-report/:id/delete',
        component: UserReportDeletePopupComponent,
        resolve: {
            userReport: UserReportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'roomieApp.userReport.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
