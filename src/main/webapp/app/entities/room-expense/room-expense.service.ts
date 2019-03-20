import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoomExpense } from 'app/shared/model/room-expense.model';

type EntityResponseType = HttpResponse<IRoomExpense>;
type EntityArrayResponseType = HttpResponse<IRoomExpense[]>;

@Injectable({ providedIn: 'root' })
export class RoomExpenseService {
    public resourceUrl = SERVER_API_URL + 'api/room-expenses';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/room-expenses';

    constructor(protected http: HttpClient) {}

    create(roomExpense: IRoomExpense): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomExpense);
        return this.http
            .post<IRoomExpense>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(roomExpense: IRoomExpense): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomExpense);
        return this.http
            .put<IRoomExpense>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRoomExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomExpense[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomExpense[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(roomExpense: IRoomExpense): IRoomExpense {
        const copy: IRoomExpense = Object.assign({}, roomExpense, {
            startDate: roomExpense.startDate != null && roomExpense.startDate.isValid() ? roomExpense.startDate.format(DATE_FORMAT) : null,
            finishDate:
                roomExpense.finishDate != null && roomExpense.finishDate.isValid() ? roomExpense.finishDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
            res.body.finishDate = res.body.finishDate != null ? moment(res.body.finishDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((roomExpense: IRoomExpense) => {
                roomExpense.startDate = roomExpense.startDate != null ? moment(roomExpense.startDate) : null;
                roomExpense.finishDate = roomExpense.finishDate != null ? moment(roomExpense.finishDate) : null;
            });
        }
        return res;
    }
}
