/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoomieTestModule } from '../../../test.module';
import { RoomPictureDeleteDialogComponent } from 'app/entities/room-picture/room-picture-delete-dialog.component';
import { RoomPictureService } from 'app/entities/room-picture/room-picture.service';

describe('Component Tests', () => {
    describe('RoomPicture Management Delete Component', () => {
        let comp: RoomPictureDeleteDialogComponent;
        let fixture: ComponentFixture<RoomPictureDeleteDialogComponent>;
        let service: RoomPictureService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomPictureDeleteDialogComponent]
            })
                .overrideTemplate(RoomPictureDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomPictureDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomPictureService);
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
