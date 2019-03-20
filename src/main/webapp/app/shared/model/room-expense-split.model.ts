import { IRoomExpenseSplitRecord } from 'app/shared/model//room-expense-split-record.model';

export interface IRoomExpenseSplit {
    id?: number;
    amount?: number;
    records?: IRoomExpenseSplitRecord[];
    expenseId?: number;
    roomieId?: number;
}

export class RoomExpenseSplit implements IRoomExpenseSplit {
    constructor(
        public id?: number,
        public amount?: number,
        public records?: IRoomExpenseSplitRecord[],
        public expenseId?: number,
        public roomieId?: number
    ) {}
}
