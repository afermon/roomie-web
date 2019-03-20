import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RoomieSharedModule } from 'app/shared';
import {
    RoomEventComponent,
    RoomEventDetailComponent,
    RoomEventUpdateComponent,
    RoomEventDeletePopupComponent,
    RoomEventDeleteDialogComponent,
    roomEventRoute,
    roomEventPopupRoute
} from './';

const ENTITY_STATES = [...roomEventRoute, ...roomEventPopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RoomEventComponent,
        RoomEventDetailComponent,
        RoomEventUpdateComponent,
        RoomEventDeleteDialogComponent,
        RoomEventDeletePopupComponent
    ],
    entryComponents: [RoomEventComponent, RoomEventUpdateComponent, RoomEventDeleteDialogComponent, RoomEventDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomEventModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
