export interface ICompany {
    id?: number;
    name?: string;
    email?: string;
    address?: string;
    phone?: string;
    premiumCost?: number;
}

export class Company implements ICompany {
    constructor(
        public id?: number,
        public name?: string,
        public email?: string,
        public address?: string,
        public phone?: string,
        public premiumCost?: number
    ) {}
}
