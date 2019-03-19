import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RoomieSharedModule } from '../../shared';

import {
    ElasticsearchReindexComponent,
    ElasticsearchReindexModalComponent,
    ElasticsearchReindexService,
    elasticsearchReindexRoute
} from './';

const ADMIN_ROUTES = [elasticsearchReindexRoute];

@NgModule({
    imports: [RoomieSharedModule, RouterModule.forChild(ADMIN_ROUTES)],
    declarations: [ElasticsearchReindexComponent, ElasticsearchReindexModalComponent],
    entryComponents: [ElasticsearchReindexModalComponent],
    providers: [ElasticsearchReindexService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieElasticsearchReindexModule {}
