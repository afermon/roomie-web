import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

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
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomieStateModule {}
