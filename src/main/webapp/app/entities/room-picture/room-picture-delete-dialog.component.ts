import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoomPicture } from 'app/shared/model/room-picture.model';
import { RoomPictureService } from './room-picture.service';

@Component({
    selector: 'jhi-room-picture-delete-dialog',
    templateUrl: './room-picture-delete-dialog.component.html'
})
export class RoomPictureDeleteDialogComponent {
    roomPicture: IRoomPicture;

    constructor(
        protected roomPictureService: RoomPictureService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roomPictureService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'roomPictureListModification',
                content: 'Deleted an roomPicture'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-room-picture-delete-popup',
    template: ''
})
export class RoomPictureDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomPicture }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RoomPictureDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.roomPicture = roomPicture;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/room-picture', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/room-picture', { outlets: { popup: null } }]);
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
