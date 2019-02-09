import { Component } from '@angular/core';
import { VERSION } from 'app/app.constants';
import { LoginModalService } from 'app/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-footer',
    templateUrl: './footer.component.html'
})
export class FooterComponent {
    version: string;
    modalRef: NgbModalRef;

    constructor(private loginModalService: LoginModalService) {
        this.version = VERSION ? 'v' + VERSION : '';
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}
