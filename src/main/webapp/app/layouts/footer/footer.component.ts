import { Component } from '@angular/core';
import { VERSION } from 'app/app.constants';
import { LoginService } from 'app/core';

@Component({
    selector: 'jhi-footer',
    templateUrl: './footer.component.html'
})
export class FooterComponent {
    version: string;
    constructor(private loginService: LoginService) {
        this.version = VERSION ? 'v' + VERSION : '';
    }

    login() {
        this.loginService.login();
    }
}
