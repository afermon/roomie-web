import { Moment } from 'moment';

export const enum NotificationType {
    APPOINTMENT = 'APPOINTMENT',
    EXPENSE = 'EXPENSE',
    TODO = 'TODO',
    EVENT = 'EVENT',
    APP = 'APP'
}

export const enum NotificationState {
    NEW = 'NEW',
    READ = 'READ'
}

export interface INotification {
    id?: number;
    created?: Moment;
    title?: string;
    body?: string;
    type?: NotificationType;
    state?: NotificationState;
    entityId?: number;
    recipientId?: number;
}

export class Notification implements INotification {
    constructor(
        public id?: number,
        public created?: Moment,
        public title?: string,
        public body?: string,
        public type?: NotificationType,
        public state?: NotificationState,
        public entityId?: number,
        public recipientId?: number
    ) {}
}
