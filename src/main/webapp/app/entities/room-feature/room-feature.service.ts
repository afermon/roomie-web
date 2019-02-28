import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoomFeature } from 'app/shared/model/room-feature.model';

type EntityResponseType = HttpResponse<IRoomFeature>;
type EntityArrayResponseType = HttpResponse<IRoomFeature[]>;

@Injectable({ providedIn: 'root' })
export class RoomFeatureService {
    public resourceUrl = SERVER_API_URL + 'api/room-features';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/room-features';

    constructor(protected http: HttpClient) {}

    create(roomFeature: IRoomFeature): Observable<EntityResponseType> {
        return this.http.post<IRoomFeature>(this.resourceUrl, roomFeature, { observe: 'response' });
    }

    update(roomFeature: IRoomFeature): Observable<EntityResponseType> {
        return this.http.put<IRoomFeature>(this.resourceUrl, roomFeature, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRoomFeature>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRoomFeature[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRoomFeature[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
