import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoomPicture } from 'app/shared/model/room-picture.model';

type EntityResponseType = HttpResponse<IRoomPicture>;
type EntityArrayResponseType = HttpResponse<IRoomPicture[]>;

@Injectable({ providedIn: 'root' })
export class RoomPictureService {
    public resourceUrl = SERVER_API_URL + 'api/room-pictures';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/room-pictures';

    constructor(protected http: HttpClient) {}

    create(roomPicture: IRoomPicture): Observable<EntityResponseType> {
        return this.http.post<IRoomPicture>(this.resourceUrl, roomPicture, { observe: 'response' });
    }

    update(roomPicture: IRoomPicture): Observable<EntityResponseType> {
        return this.http.put<IRoomPicture>(this.resourceUrl, roomPicture, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRoomPicture>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRoomPicture[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRoomPicture[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
