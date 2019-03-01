import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

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
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomPictureModule {}
