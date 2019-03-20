import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoomExpense } from 'app/shared/model/room-expense.model';
import { RoomExpenseService } from './room-expense.service';

@Component({
    selector: 'jhi-room-expense-delete-dialog',
    templateUrl: './room-expense-delete-dialog.component.html'
})
export class RoomExpenseDeleteDialogComponent {
    roomExpense: IRoomExpense;

    constructor(
        protected roomExpenseService: RoomExpenseService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roomExpenseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'roomExpenseListModification',
                content: 'Deleted an roomExpense'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-room-expense-delete-popup',
    template: ''
})
export class RoomExpenseDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomExpense }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RoomExpenseDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.roomExpense = roomExpense;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/room-expense', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/room-expense', { outlets: { popup: null } }]);
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
