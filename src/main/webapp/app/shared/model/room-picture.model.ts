export interface IRoomPicture {
    id?: number;
    url?: string;
    isMain?: boolean;
    roomId?: number;
}

export class RoomPicture implements IRoomPicture {
    constructor(public id?: number, public url?: string, public isMain?: boolean, public roomId?: number) {
        this.isMain = this.isMain || false;
    }
}
