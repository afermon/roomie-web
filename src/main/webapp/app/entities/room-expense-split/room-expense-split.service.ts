import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoomExpenseSplit } from 'app/shared/model/room-expense-split.model';

type EntityResponseType = HttpResponse<IRoomExpenseSplit>;
type EntityArrayResponseType = HttpResponse<IRoomExpenseSplit[]>;

@Injectable({ providedIn: 'root' })
export class RoomExpenseSplitService {
    public resourceUrl = SERVER_API_URL + 'api/room-expense-splits';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/room-expense-splits';

    constructor(protected http: HttpClient) {}

    create(roomExpenseSplit: IRoomExpenseSplit): Observable<EntityResponseType> {
        return this.http.post<IRoomExpenseSplit>(this.resourceUrl, roomExpenseSplit, { observe: 'response' });
    }

    update(roomExpenseSplit: IRoomExpenseSplit): Observable<EntityResponseType> {
        return this.http.put<IRoomExpenseSplit>(this.resourceUrl, roomExpenseSplit, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRoomExpenseSplit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRoomExpenseSplit[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRoomExpenseSplit[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
