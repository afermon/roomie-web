import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

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
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomExpenseSplitModule {}
