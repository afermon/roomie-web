import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { INotification } from 'app/shared/model/notification.model';

type EntityResponseType = HttpResponse<INotification>;
type EntityArrayResponseType = HttpResponse<INotification[]>;

@Injectable({ providedIn: 'root' })
export class NotificationService {
    public resourceUrl = SERVER_API_URL + 'api/notifications';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/notifications';

    constructor(protected http: HttpClient) {}

    create(notification: INotification): Observable<EntityResponseType> {
        return this.http.post<INotification>(this.resourceUrl, notification, { observe: 'response' });
    }

    update(notification: INotification): Observable<EntityResponseType> {
        return this.http.put<INotification>(this.resourceUrl, notification, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<INotification>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<INotification[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<INotification[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
