import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoomieState } from 'app/shared/model/roomie-state.model';

type EntityResponseType = HttpResponse<IRoomieState>;
type EntityArrayResponseType = HttpResponse<IRoomieState[]>;

@Injectable({ providedIn: 'root' })
export class RoomieStateService {
    public resourceUrl = SERVER_API_URL + 'api/roomie-states';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/roomie-states';

    constructor(protected http: HttpClient) {}

    create(roomieState: IRoomieState): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomieState);
        return this.http
            .post<IRoomieState>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(roomieState: IRoomieState): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomieState);
        return this.http
            .put<IRoomieState>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRoomieState>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomieState[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomieState[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(roomieState: IRoomieState): IRoomieState {
        const copy: IRoomieState = Object.assign({}, roomieState, {
            suspendedDate:
                roomieState.suspendedDate != null && roomieState.suspendedDate.isValid()
                    ? roomieState.suspendedDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.suspendedDate = res.body.suspendedDate != null ? moment(res.body.suspendedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((roomieState: IRoomieState) => {
                roomieState.suspendedDate = roomieState.suspendedDate != null ? moment(roomieState.suspendedDate) : null;
            });
        }
        return res;
    }
}
