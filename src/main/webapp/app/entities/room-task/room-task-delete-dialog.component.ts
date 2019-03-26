import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoomTask } from 'app/shared/model/room-task.model';
import { RoomTaskService } from './room-task.service';

@Component({
    selector: 'jhi-room-task-delete-dialog',
    templateUrl: './room-task-delete-dialog.component.html'
})
export class RoomTaskDeleteDialogComponent {
    roomTask: IRoomTask;

    constructor(protected roomTaskService: RoomTaskService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roomTaskService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'roomTaskListModification',
                content: 'Deleted an roomTask'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-room-task-delete-popup',
    template: ''
})
export class RoomTaskDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ roomTask }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RoomTaskDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.roomTask = roomTask;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/room-task', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/room-task', { outlets: { popup: null } }]);
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
