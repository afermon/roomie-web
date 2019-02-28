import { Moment } from 'moment';

export const enum RoomTaskState {
    PENDING = 'PENDING',
    COMPLETED = 'COMPLETED',
    LATE = 'LATE'
}

export interface IRoomTask {
    id?: number;
    created?: Moment;
    title?: string;
    description?: string;
    deadline?: Moment;
    state?: RoomTaskState;
    roomId?: number;
}

export class RoomTask implements IRoomTask {
    constructor(
        public id?: number,
        public created?: Moment,
        public title?: string,
        public description?: string,
        public deadline?: Moment,
        public state?: RoomTaskState,
        public roomId?: number
    ) {}
}
