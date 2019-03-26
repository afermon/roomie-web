import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RoomieSharedModule } from 'app/shared';
import {
    RoomExpenseComponent,
    RoomExpenseDetailComponent,
    RoomExpenseUpdateComponent,
    RoomExpenseDeletePopupComponent,
    RoomExpenseDeleteDialogComponent,
    roomExpenseRoute,
    roomExpensePopupRoute
} from './';

const ENTITY_STATES = [...roomExpenseRoute, ...roomExpensePopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RoomExpenseComponent,
        RoomExpenseDetailComponent,
        RoomExpenseUpdateComponent,
        RoomExpenseDeleteDialogComponent,
        RoomExpenseDeletePopupComponent
    ],
    entryComponents: [RoomExpenseComponent, RoomExpenseUpdateComponent, RoomExpenseDeleteDialogComponent, RoomExpenseDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomExpenseModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
