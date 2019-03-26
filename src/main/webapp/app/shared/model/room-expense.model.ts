import { Moment } from 'moment';
import { IRoomExpenseSplit } from 'app/shared/model/room-expense-split.model';

export const enum CurrencyType {
    COLON = 'COLON',
    DOLLAR = 'DOLLAR'
}

export interface IRoomExpense {
    id?: number;
    name?: string;
    description?: string;
    currency?: CurrencyType;
    amount?: number;
    periodicity?: number;
    monthDay?: number;
    startDate?: Moment;
    finishDate?: Moment;
    splits?: IRoomExpenseSplit[];
    roomId?: number;
}

export class RoomExpense implements IRoomExpense {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public currency?: CurrencyType,
        public amount?: number,
        public periodicity?: number,
        public monthDay?: number,
        public startDate?: Moment,
        public finishDate?: Moment,
        public splits?: IRoomExpenseSplit[],
        public roomId?: number
    ) {}
}
