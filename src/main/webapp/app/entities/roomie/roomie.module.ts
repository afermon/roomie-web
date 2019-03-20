import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RoomieSharedModule } from 'app/shared';
import { RoomieAdminModule } from 'app/admin/admin.module';
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
    imports: [RoomieSharedModule, RoomieAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [RoomieComponent, RoomieDetailComponent, RoomieUpdateComponent, RoomieDeleteDialogComponent, RoomieDeletePopupComponent],
    entryComponents: [RoomieComponent, RoomieUpdateComponent, RoomieDeleteDialogComponent, RoomieDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieRoomieModule {}
