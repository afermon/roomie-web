import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

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
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomExpenseModule {}
