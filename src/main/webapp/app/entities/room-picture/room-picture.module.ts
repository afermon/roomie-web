import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RoomieSharedModule } from 'app/shared';
import {
    RoomPictureComponent,
    RoomPictureDetailComponent,
    RoomPictureUpdateComponent,
    RoomPictureDeletePopupComponent,
    RoomPictureDeleteDialogComponent,
    roomPictureRoute,
    roomPicturePopupRoute
} from './';

const ENTITY_STATES = [...roomPictureRoute, ...roomPicturePopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RoomPictureComponent,
        RoomPictureDetailComponent,
        RoomPictureUpdateComponent,
        RoomPictureDeleteDialogComponent,
        RoomPictureDeletePopupComponent
    ],
    entryComponents: [RoomPictureComponent, RoomPictureUpdateComponent, RoomPictureDeleteDialogComponent, RoomPictureDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomPictureModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
