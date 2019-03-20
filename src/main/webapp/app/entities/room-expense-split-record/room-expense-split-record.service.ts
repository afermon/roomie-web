import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoomExpenseSplitRecord } from 'app/shared/model/room-expense-split-record.model';

type EntityResponseType = HttpResponse<IRoomExpenseSplitRecord>;
type EntityArrayResponseType = HttpResponse<IRoomExpenseSplitRecord[]>;

@Injectable({ providedIn: 'root' })
export class RoomExpenseSplitRecordService {
    public resourceUrl = SERVER_API_URL + 'api/room-expense-split-records';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/room-expense-split-records';

    constructor(protected http: HttpClient) {}

    create(roomExpenseSplitRecord: IRoomExpenseSplitRecord): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomExpenseSplitRecord);
        return this.http
            .post<IRoomExpenseSplitRecord>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(roomExpenseSplitRecord: IRoomExpenseSplitRecord): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomExpenseSplitRecord);
        return this.http
            .put<IRoomExpenseSplitRecord>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRoomExpenseSplitRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomExpenseSplitRecord[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomExpenseSplitRecord[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(roomExpenseSplitRecord: IRoomExpenseSplitRecord): IRoomExpenseSplitRecord {
        const copy: IRoomExpenseSplitRecord = Object.assign({}, roomExpenseSplitRecord, {
            date:
                roomExpenseSplitRecord.date != null && roomExpenseSplitRecord.date.isValid()
                    ? roomExpenseSplitRecord.date.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.date = res.body.date != null ? moment(res.body.date) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((roomExpenseSplitRecord: IRoomExpenseSplitRecord) => {
                roomExpenseSplitRecord.date = roomExpenseSplitRecord.date != null ? moment(roomExpenseSplitRecord.date) : null;
            });
        }
        return res;
    }
}
