import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RoomieSharedModule } from 'app/shared';
import {
    UserPreferencesComponent,
    UserPreferencesDetailComponent,
    UserPreferencesUpdateComponent,
    UserPreferencesDeletePopupComponent,
    UserPreferencesDeleteDialogComponent,
    userPreferencesRoute,
    userPreferencesPopupRoute
} from './';

const ENTITY_STATES = [...userPreferencesRoute, ...userPreferencesPopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserPreferencesComponent,
        UserPreferencesDetailComponent,
        UserPreferencesUpdateComponent,
        UserPreferencesDeleteDialogComponent,
        UserPreferencesDeletePopupComponent
    ],
    entryComponents: [
        UserPreferencesComponent,
        UserPreferencesUpdateComponent,
        UserPreferencesDeleteDialogComponent,
        UserPreferencesDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieUserPreferencesModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
