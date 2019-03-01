import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RoomieSharedModule } from 'app/shared';
import {
    UserReportComponent,
    UserReportDetailComponent,
    UserReportUpdateComponent,
    UserReportDeletePopupComponent,
    UserReportDeleteDialogComponent,
    userReportRoute,
    userReportPopupRoute
} from './';

const ENTITY_STATES = [...userReportRoute, ...userReportPopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserReportComponent,
        UserReportDetailComponent,
        UserReportUpdateComponent,
        UserReportDeleteDialogComponent,
        UserReportDeletePopupComponent
    ],
    entryComponents: [UserReportComponent, UserReportUpdateComponent, UserReportDeleteDialogComponent, UserReportDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieUserReportModule {}
