export interface IAddress {
    id?: number;
    location?: string;
    city?: string;
    state?: string;
    description?: string;
}

export class Address implements IAddress {
    constructor(public id?: number, public location?: string, public city?: string, public state?: string, public description?: string) {}
}
