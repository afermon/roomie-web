import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RoomieSharedModule } from 'app/shared';
import {
    RoomTaskComponent,
    RoomTaskDetailComponent,
    RoomTaskUpdateComponent,
    RoomTaskDeletePopupComponent,
    RoomTaskDeleteDialogComponent,
    roomTaskRoute,
    roomTaskPopupRoute
} from './';

const ENTITY_STATES = [...roomTaskRoute, ...roomTaskPopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RoomTaskComponent,
        RoomTaskDetailComponent,
        RoomTaskUpdateComponent,
        RoomTaskDeleteDialogComponent,
        RoomTaskDeletePopupComponent
    ],
    entryComponents: [RoomTaskComponent, RoomTaskUpdateComponent, RoomTaskDeleteDialogComponent, RoomTaskDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomTaskModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
