import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

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
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieUserReportModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
