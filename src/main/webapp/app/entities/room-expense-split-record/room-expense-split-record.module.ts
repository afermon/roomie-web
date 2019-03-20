import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RoomieSharedModule } from 'app/shared';
import {
    RoomExpenseSplitRecordComponent,
    RoomExpenseSplitRecordDetailComponent,
    RoomExpenseSplitRecordUpdateComponent,
    RoomExpenseSplitRecordDeletePopupComponent,
    RoomExpenseSplitRecordDeleteDialogComponent,
    roomExpenseSplitRecordRoute,
    roomExpenseSplitRecordPopupRoute
} from './';

const ENTITY_STATES = [...roomExpenseSplitRecordRoute, ...roomExpenseSplitRecordPopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RoomExpenseSplitRecordComponent,
        RoomExpenseSplitRecordDetailComponent,
        RoomExpenseSplitRecordUpdateComponent,
        RoomExpenseSplitRecordDeleteDialogComponent,
        RoomExpenseSplitRecordDeletePopupComponent
    ],
    entryComponents: [
        RoomExpenseSplitRecordComponent,
        RoomExpenseSplitRecordUpdateComponent,
        RoomExpenseSplitRecordDeleteDialogComponent,
        RoomExpenseSplitRecordDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomExpenseSplitRecordModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
