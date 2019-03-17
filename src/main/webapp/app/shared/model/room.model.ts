import { Moment } from 'moment';
import { IAppointment } from 'app/shared/model//appointment.model';
import { IRoomTask } from 'app/shared/model//room-task.model';
import { IRoomEvent } from 'app/shared/model//room-event.model';
import { IRoomie } from 'app/shared/model//roomie.model';
import { IRoomFeature } from 'app/shared/model//room-feature.model';
import { IRoomExpense } from 'app/shared/model//room-expense.model';
import { IRoomPicture } from 'app/shared/model//room-picture.model';

export const enum RoomState {
    SEARCH = 'SEARCH',
    PREMIUM = 'PREMIUM'
}

export const enum RoomType {
    ROOM = 'ROOM',
    APARTMENT = 'APARTMENT',
    HOUSE = 'HOUSE'
}

export interface IRoom {
    id?: number;
    state?: RoomState;
    created?: Moment;
    published?: Moment;
    title?: string;
    description?: string;
    rooms?: number;
    roomType?: RoomType;
    apoinmentsNotes?: string;
    lookingForRoomie?: boolean;
    availableFrom?: Moment;
    isPremium?: boolean;
    addressId?: number;
    appointments?: IAppointment[];
    roomTasks?: IRoomTask[];
    roomEvents?: IRoomEvent[];
    roomies?: IRoomie[];
    features?: IRoomFeature[];
    ownerId?: number;
    expenses?: IRoomExpense[];
    pictures?: IRoomPicture[];
}

export class Room implements IRoom {
    constructor(
        public id?: number,
        public state?: RoomState,
        public created?: Moment,
        public published?: Moment,
        public title?: string,
        public description?: string,
        public rooms?: number,
        public roomType?: RoomType,
        public apoinmentsNotes?: string,
        public lookingForRoomie?: boolean,
        public availableFrom?: Moment,
        public isPremium?: boolean,
        public addressId?: number,
        public appointments?: IAppointment[],
        public roomTasks?: IRoomTask[],
        public roomEvents?: IRoomEvent[],
        public roomies?: IRoomie[],
        public features?: IRoomFeature[],
        public ownerId?: number,
        public expenses?: IRoomExpense[],
        public pictures?: IRoomPicture[]
    ) {
        this.lookingForRoomie = this.lookingForRoomie || false;
        this.isPremium = this.isPremium || false;
    }
}
