import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { RoomieSharedLibsModule, RoomieSharedCommonModule, HasAnyAuthorityDirective } from './';

@NgModule({
    imports: [RoomieSharedLibsModule, RoomieSharedCommonModule],
    declarations: [HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    exports: [RoomieSharedCommonModule, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RoomieSharedModule {
    static forRoot() {
        return {
            ngModule: RoomieSharedModule
        };
    }
}
