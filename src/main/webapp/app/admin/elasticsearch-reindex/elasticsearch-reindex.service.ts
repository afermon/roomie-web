import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable()
export class ElasticsearchReindexService {
    constructor(private http: HttpClient) {}

    reindex(): Observable<HttpResponse<any>> {
        return this.http.post<any>('api/elasticsearch/index', { observe: 'response' });
    }
}
