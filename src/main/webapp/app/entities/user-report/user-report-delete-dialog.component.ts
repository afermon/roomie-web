import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserReport } from 'app/shared/model/user-report.model';
import { UserReportService } from './user-report.service';

@Component({
    selector: 'jhi-user-report-delete-dialog',
    templateUrl: './user-report-delete-dialog.component.html'
})
export class UserReportDeleteDialogComponent {
    userReport: IUserReport;

    constructor(
        protected userReportService: UserReportService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userReportService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'userReportListModification',
                content: 'Deleted an userReport'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-report-delete-popup',
    template: ''
})
export class UserReportDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userReport }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(UserReportDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.userReport = userReport;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/user-report', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/user-report', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
