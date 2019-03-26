import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RoomieSharedModule } from 'app/shared';
import {
    RoomFeatureComponent,
    RoomFeatureDetailComponent,
    RoomFeatureUpdateComponent,
    RoomFeatureDeletePopupComponent,
    RoomFeatureDeleteDialogComponent,
    roomFeatureRoute,
    roomFeaturePopupRoute
} from './';

const ENTITY_STATES = [...roomFeatureRoute, ...roomFeaturePopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RoomFeatureComponent,
        RoomFeatureDetailComponent,
        RoomFeatureUpdateComponent,
        RoomFeatureDeleteDialogComponent,
        RoomFeatureDeletePopupComponent
    ],
    entryComponents: [RoomFeatureComponent, RoomFeatureUpdateComponent, RoomFeatureDeleteDialogComponent, RoomFeatureDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomFeatureModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
