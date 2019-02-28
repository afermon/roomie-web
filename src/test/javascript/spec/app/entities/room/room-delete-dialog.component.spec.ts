/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoomieTestModule } from '../../../test.module';
import { RoomDeleteDialogComponent } from 'app/entities/room/room-delete-dialog.component';
import { RoomService } from 'app/entities/room/room.service';

describe('Component Tests', () => {
    describe('Room Management Delete Component', () => {
        let comp: RoomDeleteDialogComponent;
        let fixture: ComponentFixture<RoomDeleteDialogComponent>;
        let service: RoomService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomDeleteDialogComponent]
            })
                .overrideTemplate(RoomDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
