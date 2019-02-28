import { Moment } from 'moment';

export interface IRoomExpenseSplitRecord {
    id?: number;
    date?: Moment;
    state?: string;
    splitId?: number;
}

export class RoomExpenseSplitRecord implements IRoomExpenseSplitRecord {
    constructor(public id?: number, public date?: Moment, public state?: string, public splitId?: number) {}
}
