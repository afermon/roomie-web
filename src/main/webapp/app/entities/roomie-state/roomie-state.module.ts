import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RoomieSharedModule } from 'app/shared';
import {
    RoomieStateComponent,
    RoomieStateDetailComponent,
    RoomieStateUpdateComponent,
    RoomieStateDeletePopupComponent,
    RoomieStateDeleteDialogComponent,
    roomieStateRoute,
    roomieStatePopupRoute
} from './';

const ENTITY_STATES = [...roomieStateRoute, ...roomieStatePopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RoomieStateComponent,
        RoomieStateDetailComponent,
        RoomieStateUpdateComponent,
        RoomieStateDeleteDialogComponent,
        RoomieStateDeletePopupComponent
    ],
    entryComponents: [RoomieStateComponent, RoomieStateUpdateComponent, RoomieStateDeleteDialogComponent, RoomieStateDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomieStateModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
