import { Moment } from 'moment';

export interface IRoomEvent {
    id?: number;
    title?: string;
    description?: string;
    isPrivate?: boolean;
    startTime?: Moment;
    endTime?: Moment;
    roomId?: number;
    organizerId?: number;
}

export class RoomEvent implements IRoomEvent {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public isPrivate?: boolean,
        public startTime?: Moment,
        public endTime?: Moment,
        public roomId?: number,
        public organizerId?: number
    ) {
        this.isPrivate = this.isPrivate || false;
    }
}
