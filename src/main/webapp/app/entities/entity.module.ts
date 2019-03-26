import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'roomie',
                loadChildren: './roomie/roomie.module#RoomieRoomieModule'
            },
            {
                path: 'roomie-state',
                loadChildren: './roomie-state/roomie-state.module#RoomieRoomieStateModule'
            },
            {
                path: 'address',
                loadChildren: './address/address.module#RoomieAddressModule'
            },
            {
                path: 'user-preferences',
                loadChildren: './user-preferences/user-preferences.module#RoomieUserPreferencesModule'
            },
            {
                path: 'room',
                loadChildren: './room/room.module#RoomieRoomModule'
            },
            {
                path: 'room-picture',
                loadChildren: './room-picture/room-picture.module#RoomieRoomPictureModule'
            },
            {
                path: 'room-feature',
                loadChildren: './room-feature/room-feature.module#RoomieRoomFeatureModule'
            },
            {
                path: 'room-expense',
                loadChildren: './room-expense/room-expense.module#RoomieRoomExpenseModule'
            },
            {
                path: 'room-expense-split',
                loadChildren: './room-expense-split/room-expense-split.module#RoomieRoomExpenseSplitModule'
            },
            {
                path: 'room-expense-split-record',
                loadChildren: './room-expense-split-record/room-expense-split-record.module#RoomieRoomExpenseSplitRecordModule'
            },
            {
                path: 'appointment',
                loadChildren: './appointment/appointment.module#RoomieAppointmentModule'
            },
            {
                path: 'room-task',
                loadChildren: './room-task/room-task.module#RoomieRoomTaskModule'
            },
            {
                path: 'room-event',
                loadChildren: './room-event/room-event.module#RoomieRoomEventModule'
            },
            {
                path: 'user-report',
                loadChildren: './user-report/user-report.module#RoomieUserReportModule'
            },
            {
                path: 'company',
                loadChildren: './company/company.module#RoomieCompanyModule'
            },
            {
                path: 'notification',
                loadChildren: './notification/notification.module#RoomieNotificationModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieEntityModule {}
