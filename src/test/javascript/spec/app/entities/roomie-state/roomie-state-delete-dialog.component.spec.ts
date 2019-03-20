/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoomieTestModule } from '../../../test.module';
import { RoomieStateDeleteDialogComponent } from 'app/entities/roomie-state/roomie-state-delete-dialog.component';
import { RoomieStateService } from 'app/entities/roomie-state/roomie-state.service';

describe('Component Tests', () => {
    describe('RoomieState Management Delete Component', () => {
        let comp: RoomieStateDeleteDialogComponent;
        let fixture: ComponentFixture<RoomieStateDeleteDialogComponent>;
        let service: RoomieStateService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomieStateDeleteDialogComponent]
            })
                .overrideTemplate(RoomieStateDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomieStateDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomieStateService);
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
