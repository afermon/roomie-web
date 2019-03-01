export interface IAddress {
    id?: number;
    latitude?: number;
    longitude?: number;
    city?: string;
    state?: string;
    description?: string;
}

export class Address implements IAddress {
    constructor(
        public id?: number,
        public latitude?: number,
        public longitude?: number,
        public city?: string,
        public state?: string,
        public description?: string
    ) {}
}
