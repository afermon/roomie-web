import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RoomieSharedModule } from 'app/shared';
import {
    RoomExpenseSplitComponent,
    RoomExpenseSplitDetailComponent,
    RoomExpenseSplitUpdateComponent,
    RoomExpenseSplitDeletePopupComponent,
    RoomExpenseSplitDeleteDialogComponent,
    roomExpenseSplitRoute,
    roomExpenseSplitPopupRoute
} from './';

const ENTITY_STATES = [...roomExpenseSplitRoute, ...roomExpenseSplitPopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RoomExpenseSplitComponent,
        RoomExpenseSplitDetailComponent,
        RoomExpenseSplitUpdateComponent,
        RoomExpenseSplitDeleteDialogComponent,
        RoomExpenseSplitDeletePopupComponent
    ],
    entryComponents: [
        RoomExpenseSplitComponent,
        RoomExpenseSplitUpdateComponent,
        RoomExpenseSplitDeleteDialogComponent,
        RoomExpenseSplitDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomExpenseSplitModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
