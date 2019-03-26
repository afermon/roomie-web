import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoomExpenseSplitRecord } from 'app/shared/model/room-expense-split-record.model';
import { RoomExpenseSplitRecordService } from './room-expense-split-record.service';

@Component({
    selector: 'jhi-room-expense-split-record-delete-dialog',
    templateUrl: './room-expense-split-record-delete-dialog.component.html'
})
export class RoomExpenseSplitRecordDeleteDialogComponent {
    roomExpenseSplitRecord: IRoomExpenseSplitRecord;

    constructor(
        protected roomExpenseSplitRecordService: RoomExpenseSplitRecordService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roomExpenseSplitRecordService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'roomExpenseSplitRecordListModification',
                content: 'Deleted an roomExpenseSplitRecord'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-room-expense-split-record-delete-popup',
    template: ''
})
export class RoomExpenseSplitRecordDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomExpenseSplitRecord }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RoomExpenseSplitRecordDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.roomExpenseSplitRecord = roomExpenseSplitRecord;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/room-expense-split-record', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/room-expense-split-record', { outlets: { popup: null } }]);
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
