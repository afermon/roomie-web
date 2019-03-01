/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoomieTestModule } from '../../../test.module';
import { RoomFeatureDeleteDialogComponent } from 'app/entities/room-feature/room-feature-delete-dialog.component';
import { RoomFeatureService } from 'app/entities/room-feature/room-feature.service';

describe('Component Tests', () => {
    describe('RoomFeature Management Delete Component', () => {
        let comp: RoomFeatureDeleteDialogComponent;
        let fixture: ComponentFixture<RoomFeatureDeleteDialogComponent>;
        let service: RoomFeatureService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomFeatureDeleteDialogComponent]
            })
                .overrideTemplate(RoomFeatureDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomFeatureDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomFeatureService);
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
