export const enum CurrencyType {
    COLON = 'COLON',
    DOLLAR = 'DOLLAR'
}

export interface IUserPreferences {
    id?: number;
    currency?: CurrencyType;
    todoListNotifications?: boolean;
    calendarNotifications?: boolean;
    paymentsNotifications?: boolean;
    appointmentsNotifications?: boolean;
}

export class UserPreferences implements IUserPreferences {
    constructor(
        public id?: number,
        public currency?: CurrencyType,
        public todoListNotifications?: boolean,
        public calendarNotifications?: boolean,
        public paymentsNotifications?: boolean,
        public appointmentsNotifications?: boolean
    ) {
        this.todoListNotifications = this.todoListNotifications || false;
        this.calendarNotifications = this.calendarNotifications || false;
        this.paymentsNotifications = this.paymentsNotifications || false;
        this.appointmentsNotifications = this.appointmentsNotifications || false;
    }
}
