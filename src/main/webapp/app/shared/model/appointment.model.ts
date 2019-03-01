import { Moment } from 'moment';

export const enum AppointmentState {
    ACCEPTED = 'ACCEPTED',
    DECLINED = 'DECLINED',
    PENDING = 'PENDING'
}

export interface IAppointment {
    id?: number;
    description?: string;
    dateTime?: Moment;
    state?: AppointmentState;
    petitionerId?: number;
    roomId?: number;
}

export class Appointment implements IAppointment {
    constructor(
        public id?: number,
        public description?: string,
        public dateTime?: Moment,
        public state?: AppointmentState,
        public petitionerId?: number,
        public roomId?: number
    ) {}
}
