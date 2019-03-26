import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoomieState } from 'app/shared/model/roomie-state.model';
import { RoomieStateService } from './roomie-state.service';

@Component({
    selector: 'jhi-roomie-state-delete-dialog',
    templateUrl: './roomie-state-delete-dialog.component.html'
})
export class RoomieStateDeleteDialogComponent {
    roomieState: IRoomieState;

    constructor(
        protected roomieStateService: RoomieStateService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roomieStateService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'roomieStateListModification',
                content: 'Deleted an roomieState'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-roomie-state-delete-popup',
    template: ''
})
export class RoomieStateDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomieState }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RoomieStateDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.roomieState = roomieState;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/roomie-state', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/roomie-state', { outlets: { popup: null } }]);
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
