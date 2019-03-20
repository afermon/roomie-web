import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RoomieSharedModule } from 'app/shared';
import {
    UserPreferencesComponent,
    UserPreferencesDetailComponent,
    UserPreferencesUpdateComponent,
    UserPreferencesDeletePopupComponent,
    UserPreferencesDeleteDialogComponent,
    userPreferencesRoute,
    userPreferencesPopupRoute
} from './';

const ENTITY_STATES = [...userPreferencesRoute, ...userPreferencesPopupRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserPreferencesComponent,
        UserPreferencesDetailComponent,
        UserPreferencesUpdateComponent,
        UserPreferencesDeleteDialogComponent,
        UserPreferencesDeletePopupComponent
    ],
    entryComponents: [
        UserPreferencesComponent,
        UserPreferencesUpdateComponent,
        UserPreferencesDeleteDialogComponent,
        UserPreferencesDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieUserPreferencesModule {}
