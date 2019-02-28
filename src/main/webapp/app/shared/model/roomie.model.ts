import { Moment } from 'moment';
import { IRoomExpenseSplit } from 'app/shared/model//room-expense-split.model';
import { IRoom } from 'app/shared/model//room.model';
import { IRoomEvent } from 'app/shared/model//room-event.model';
import { IRoomFeature } from 'app/shared/model//room-feature.model';

export const enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE',
    OTHER = 'OTHER'
}

export interface IRoomie {
    id?: number;
    birthDate?: Moment;
    biography?: string;
    picture?: string;
    gender?: Gender;
    phone?: string;
    mobileDeviceID?: string;
    userId?: number;
    stateId?: number;
    addressId?: number;
    configurationId?: number;
    roomExpenseSplits?: IRoomExpenseSplit[];
    rooms?: IRoom[];
    roomEvents?: IRoomEvent[];
    lifestyles?: IRoomFeature[];
}

export class Roomie implements IRoomie {
    constructor(
        public id?: number,
        public birthDate?: Moment,
        public biography?: string,
        public picture?: string,
        public gender?: Gender,
        public phone?: string,
        public mobileDeviceID?: string,
        public userId?: number,
        public stateId?: number,
        public addressId?: number,
        public configurationId?: number,
        public roomExpenseSplits?: IRoomExpenseSplit[],
        public rooms?: IRoom[],
        public roomEvents?: IRoomEvent[],
        public lifestyles?: IRoomFeature[]
    ) {}
}
