import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoomExpenseSplit } from 'app/shared/model/room-expense-split.model';
import { RoomExpenseSplitService } from './room-expense-split.service';

@Component({
    selector: 'jhi-room-expense-split-delete-dialog',
    templateUrl: './room-expense-split-delete-dialog.component.html'
})
export class RoomExpenseSplitDeleteDialogComponent {
    roomExpenseSplit: IRoomExpenseSplit;

    constructor(
        protected roomExpenseSplitService: RoomExpenseSplitService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roomExpenseSplitService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'roomExpenseSplitListModification',
                content: 'Deleted an roomExpenseSplit'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-room-expense-split-delete-popup',
    template: ''
})
export class RoomExpenseSplitDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomExpenseSplit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RoomExpenseSplitDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.roomExpenseSplit = roomExpenseSplit;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
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
