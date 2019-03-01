import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

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
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomTaskModule {}
