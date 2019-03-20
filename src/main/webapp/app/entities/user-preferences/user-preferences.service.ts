import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserPreferences } from 'app/shared/model/user-preferences.model';

type EntityResponseType = HttpResponse<IUserPreferences>;
type EntityArrayResponseType = HttpResponse<IUserPreferences[]>;

@Injectable({ providedIn: 'root' })
export class UserPreferencesService {
    public resourceUrl = SERVER_API_URL + 'api/user-preferences';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/user-preferences';

    constructor(protected http: HttpClient) {}

    create(userPreferences: IUserPreferences): Observable<EntityResponseType> {
        return this.http.post<IUserPreferences>(this.resourceUrl, userPreferences, { observe: 'response' });
    }

    update(userPreferences: IUserPreferences): Observable<EntityResponseType> {
        return this.http.put<IUserPreferences>(this.resourceUrl, userPreferences, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IUserPreferences>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUserPreferences[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUserPreferences[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
