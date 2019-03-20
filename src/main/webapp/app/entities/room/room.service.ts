import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoom } from 'app/shared/model/room.model';

type EntityResponseType = HttpResponse<IRoom>;
type EntityArrayResponseType = HttpResponse<IRoom[]>;

@Injectable({ providedIn: 'root' })
export class RoomService {
    public resourceUrl = SERVER_API_URL + 'api/rooms';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/rooms';

    constructor(protected http: HttpClient) {}

    create(room: IRoom): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(room);
        return this.http
            .post<IRoom>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(room: IRoom): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(room);
        return this.http
            .put<IRoom>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRoom>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoom[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoom[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(room: IRoom): IRoom {
        const copy: IRoom = Object.assign({}, room, {
            created: room.created != null && room.created.isValid() ? room.created.toJSON() : null,
            published: room.published != null && room.published.isValid() ? room.published.toJSON() : null,
            availableFrom: room.availableFrom != null && room.availableFrom.isValid() ? room.availableFrom.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.created = res.body.created != null ? moment(res.body.created) : null;
            res.body.published = res.body.published != null ? moment(res.body.published) : null;
            res.body.availableFrom = res.body.availableFrom != null ? moment(res.body.availableFrom) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((room: IRoom) => {
                room.created = room.created != null ? moment(room.created) : null;
                room.published = room.published != null ? moment(room.published) : null;
                room.availableFrom = room.availableFrom != null ? moment(room.availableFrom) : null;
            });
        }
        return res;
    }
}
