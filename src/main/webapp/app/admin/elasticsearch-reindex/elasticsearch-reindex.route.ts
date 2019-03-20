import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { ElasticsearchReindexComponent } from './elasticsearch-reindex.component';

export const elasticsearchReindexRoute: Route = {
    path: 'elasticsearch-reindex',
    component: ElasticsearchReindexComponent,
    data: {
        authorities: ['ROLE_ADMIN'],
        pageTitle: 'elasticsearch.reindex.title'
    },
    canActivate: [UserRouteAccessService]
};
