import { Moment } from 'moment';

export const enum AccountState {
    ACTIVE = 'ACTIVE',
    SUSPENDED = 'SUSPENDED',
    BANNED = 'BANNED'
}

export interface IRoomieState {
    id?: number;
    state?: AccountState;
    suspendedDate?: Moment;
}

export class RoomieState implements IRoomieState {
    constructor(public id?: number, public state?: AccountState, public suspendedDate?: Moment) {}
}
