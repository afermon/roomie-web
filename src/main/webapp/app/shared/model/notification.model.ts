export const enum NotificationType {
    APPOINTMENT = 'APPOINTMENT',
    EXPENSE = 'EXPENSE',
    TODO = 'TODO',
    EVENT = 'EVENT'
}

export interface INotification {
    id?: number;
    title?: string;
    body?: string;
    type?: NotificationType;
    entityId?: number;
    recipientId?: number;
}

export class Notification implements INotification {
    constructor(
        public id?: number,
        public title?: string,
        public body?: string,
        public type?: NotificationType,
        public entityId?: number,
        public recipientId?: number
    ) {}
}
