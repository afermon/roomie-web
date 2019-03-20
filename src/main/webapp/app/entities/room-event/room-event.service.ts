import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoomEvent } from 'app/shared/model/room-event.model';

type EntityResponseType = HttpResponse<IRoomEvent>;
type EntityArrayResponseType = HttpResponse<IRoomEvent[]>;

@Injectable({ providedIn: 'root' })
export class RoomEventService {
    public resourceUrl = SERVER_API_URL + 'api/room-events';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/room-events';

    constructor(protected http: HttpClient) {}

    create(roomEvent: IRoomEvent): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomEvent);
        return this.http
            .post<IRoomEvent>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(roomEvent: IRoomEvent): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomEvent);
        return this.http
            .put<IRoomEvent>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRoomEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomEvent[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(roomEvent: IRoomEvent): IRoomEvent {
        const copy: IRoomEvent = Object.assign({}, roomEvent, {
            startTime: roomEvent.startTime != null && roomEvent.startTime.isValid() ? roomEvent.startTime.toJSON() : null,
            endTime: roomEvent.endTime != null && roomEvent.endTime.isValid() ? roomEvent.endTime.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.startTime = res.body.startTime != null ? moment(res.body.startTime) : null;
            res.body.endTime = res.body.endTime != null ? moment(res.body.endTime) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((roomEvent: IRoomEvent) => {
                roomEvent.startTime = roomEvent.startTime != null ? moment(roomEvent.startTime) : null;
                roomEvent.endTime = roomEvent.endTime != null ? moment(roomEvent.endTime) : null;
            });
        }
        return res;
    }
}
