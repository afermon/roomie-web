import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoomTask } from 'app/shared/model/room-task.model';

type EntityResponseType = HttpResponse<IRoomTask>;
type EntityArrayResponseType = HttpResponse<IRoomTask[]>;

@Injectable({ providedIn: 'root' })
export class RoomTaskService {
    public resourceUrl = SERVER_API_URL + 'api/room-tasks';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/room-tasks';

    constructor(protected http: HttpClient) {}

    create(roomTask: IRoomTask): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomTask);
        return this.http
            .post<IRoomTask>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(roomTask: IRoomTask): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(roomTask);
        return this.http
            .put<IRoomTask>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRoomTask>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomTask[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRoomTask[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(roomTask: IRoomTask): IRoomTask {
        const copy: IRoomTask = Object.assign({}, roomTask, {
            created: roomTask.created != null && roomTask.created.isValid() ? roomTask.created.toJSON() : null,
            deadline: roomTask.deadline != null && roomTask.deadline.isValid() ? roomTask.deadline.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.created = res.body.created != null ? moment(res.body.created) : null;
            res.body.deadline = res.body.deadline != null ? moment(res.body.deadline) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((roomTask: IRoomTask) => {
                roomTask.created = roomTask.created != null ? moment(roomTask.created) : null;
                roomTask.deadline = roomTask.deadline != null ? moment(roomTask.deadline) : null;
            });
        }
        return res;
    }
}
