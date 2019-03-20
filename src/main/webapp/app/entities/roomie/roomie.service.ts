import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoomie } from 'app/shared/model/roomie.model';

type EntityResponseType = HttpResponse<IRoomie>;
type EntityArrayResponseType = HttpResponse<IRoomie[]>;

@Injectable({ providedIn: 'root' })
export class RoomieService {
    public resourceUrl = SERVER_API_URL + 'api/roomies';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/roomies';

    constructor(protected http: HttpClient) {}

    create(roomie: IRoomie): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomie);
        return this.http
            .post<IRoomie>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(roomie: IRoomie): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomie);
        return this.http
            .put<IRoomie>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRoomie>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomie[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomie[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(roomie: IRoomie): IRoomie {
        const copy: IRoomie = Object.assign({}, roomie, {
            birthDate: roomie.birthDate != null && roomie.birthDate.isValid() ? roomie.birthDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.birthDate = res.body.birthDate != null ? moment(res.body.birthDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((roomie: IRoomie) => {
                roomie.birthDate = roomie.birthDate != null ? moment(roomie.birthDate) : null;
            });
        }
        return res;
    }
}
