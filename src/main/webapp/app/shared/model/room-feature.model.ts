export const enum Lang {
    ESP = 'ESP',
    ENG = 'ENG'
}

export const enum FeatureType {
    AMENITIES = 'AMENITIES',
    RESTRICTIONS = 'RESTRICTIONS',
    LIFESTYLE = 'LIFESTYLE'
}

export interface IRoomFeature {
    id?: number;
    lang?: Lang;
    type?: FeatureType;
    name?: string;
    icon?: string;
    description?: string;
}

export class RoomFeature implements IRoomFeature {
    constructor(
        public id?: number,
        public lang?: Lang,
        public type?: FeatureType,
        public name?: string,
        public icon?: string,
        public description?: string
    ) {}
}
