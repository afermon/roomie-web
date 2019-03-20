/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoomieTestModule } from '../../../test.module';
import { RoomExpenseDeleteDialogComponent } from 'app/entities/room-expense/room-expense-delete-dialog.component';
import { RoomExpenseService } from 'app/entities/room-expense/room-expense.service';

describe('Component Tests', () => {
    describe('RoomExpense Management Delete Component', () => {
        let comp: RoomExpenseDeleteDialogComponent;
        let fixture: ComponentFixture<RoomExpenseDeleteDialogComponent>;
        let service: RoomExpenseService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomExpenseDeleteDialogComponent]
            })
                .overrideTemplate(RoomExpenseDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomExpenseDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomExpenseService);
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
