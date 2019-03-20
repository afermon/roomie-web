import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoomEvent } from 'app/shared/model/room-event.model';
import { RoomEventService } from './room-event.service';

@Component({
    selector: 'jhi-room-event-delete-dialog',
    templateUrl: './room-event-delete-dialog.component.html'
})
export class RoomEventDeleteDialogComponent {
    roomEvent: IRoomEvent;

    constructor(
        protected roomEventService: RoomEventService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roomEventService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'roomEventListModification',
                content: 'Deleted an roomEvent'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-room-event-delete-popup',
    template: ''
})
export class RoomEventDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomEvent }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RoomEventDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.roomEvent = roomEvent;
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
