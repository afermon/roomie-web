import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { RoomieRoomieModule } from './roomie/roomie.module';
import { RoomieRoomieStateModule } from './roomie-state/roomie-state.module';
import { RoomieAddressModule } from './address/address.module';
import { RoomieUserPreferencesModule } from './user-preferences/user-preferences.module';
import { RoomieRoomModule } from './room/room.module';
import { RoomieRoomPictureModule } from './room-picture/room-picture.module';
import { RoomieRoomFeatureModule } from './room-feature/room-feature.module';
import { RoomieRoomExpenseModule } from './room-expense/room-expense.module';
import { RoomieRoomExpenseSplitModule } from './room-expense-split/room-expense-split.module';
import { RoomieRoomExpenseSplitRecordModule } from './room-expense-split-record/room-expense-split-record.module';
import { RoomieAppointmentModule } from './appointment/appointment.module';
import { RoomieRoomTaskModule } from './room-task/room-task.module';
import { RoomieRoomEventModule } from './room-event/room-event.module';
import { RoomieUserReportModule } from './user-report/user-report.module';
import { RoomieCompanyModule } from './company/company.module';
import { RoomieNotificationModule } from './notification/notification.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        RoomieRoomieModule,
        RoomieRoomieStateModule,
        RoomieAddressModule,
        RoomieUserPreferencesModule,
        RoomieRoomModule,
        RoomieRoomPictureModule,
        RoomieRoomFeatureModule,
        RoomieRoomExpenseModule,
        RoomieRoomExpenseSplitModule,
        RoomieRoomExpenseSplitRecordModule,
        RoomieAppointmentModule,
        RoomieRoomTaskModule,
        RoomieRoomEventModule,
        RoomieUserReportModule,
        RoomieCompanyModule,
        RoomieNotificationModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieEntityModule {}
