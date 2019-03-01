import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserReport } from 'app/shared/model/user-report.model';

type EntityResponseType = HttpResponse<IUserReport>;
type EntityArrayResponseType = HttpResponse<IUserReport[]>;

@Injectable({ providedIn: 'root' })
export class UserReportService {
    public resourceUrl = SERVER_API_URL + 'api/user-reports';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/user-reports';

    constructor(protected http: HttpClient) {}

    create(userReport: IUserReport): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(userReport);
        return this.http
            .post<IUserReport>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(userReport: IUserReport): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(userReport);
        return this.http
            .put<IUserReport>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IUserReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IUserReport[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IUserReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(userReport: IUserReport): IUserReport {
        const copy: IUserReport = Object.assign({}, userReport, {
            date: userReport.date != null && userReport.date.isValid() ? userReport.date.format(DATE_FORMAT) : null
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
            res.body.forEach((userReport: IUserReport) => {
                userReport.date = userReport.date != null ? moment(userReport.date) : null;
            });
        }
        return res;
    }
}
