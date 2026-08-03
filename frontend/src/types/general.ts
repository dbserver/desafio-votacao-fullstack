export type RequestStatus = 'LOADING' | 'ERROR' | 'SUCCESS';

export type AlertType = 'success' | 'info' | 'danger' | 'warning' | '';

export interface AlertState {
    message: string;
    type: AlertType;
}
