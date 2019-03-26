import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RoomieSharedModule } from 'app/shared';
import {
    RoomieComponent,
    RoomieDetailComponent,
    RoomieUpdateComponent,
    RoomieDeletePopupComponent,
    RoomieDeleteDialogComponent,
    roomieRoute,
    roomiePopupRoute
} from './';

const ENTITY_STATES = [...roomieRoute, ...roomiePopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [RoomieComponent, RoomieDetailComponent, RoomieUpdateComponent, RoomieDeleteDialogComponent, RoomieDeletePopupComponent],
    entryComponents: [RoomieComponent, RoomieUpdateComponent, RoomieDeleteDialogComponent, RoomieDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomieModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
