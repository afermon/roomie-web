import { Moment } from 'moment';

export const enum ReportType {
    USER = 'USER',
    ROOM = 'ROOM',
    APP = 'APP'
}

export interface IUserReport {
    id?: number;
    date?: Moment;
    description?: string;
    type?: ReportType;
    roomieId?: number;
    roomId?: number;
}

export class UserReport implements IUserReport {
    constructor(
        public id?: number,
        public date?: Moment,
        public description?: string,
        public type?: ReportType,
        public roomieId?: number,
        public roomId?: number
    ) {}
}
