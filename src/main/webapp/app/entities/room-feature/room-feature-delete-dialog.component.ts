import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoomFeature } from 'app/shared/model/room-feature.model';
import { RoomFeatureService } from './room-feature.service';

@Component({
    selector: 'jhi-room-feature-delete-dialog',
    templateUrl: './room-feature-delete-dialog.component.html'
})
export class RoomFeatureDeleteDialogComponent {
    roomFeature: IRoomFeature;

    constructor(
        protected roomFeatureService: RoomFeatureService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roomFeatureService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'roomFeatureListModification',
                content: 'Deleted an roomFeature'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-room-feature-delete-popup',
    template: ''
})
export class RoomFeatureDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomFeature }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RoomFeatureDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.roomFeature = roomFeature;
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
