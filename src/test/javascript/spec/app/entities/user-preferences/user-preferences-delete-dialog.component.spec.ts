/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoomieTestModule } from '../../../test.module';
import { UserPreferencesDeleteDialogComponent } from 'app/entities/user-preferences/user-preferences-delete-dialog.component';
import { UserPreferencesService } from 'app/entities/user-preferences/user-preferences.service';

describe('Component Tests', () => {
    describe('UserPreferences Management Delete Component', () => {
        let comp: UserPreferencesDeleteDialogComponent;
        let fixture: ComponentFixture<UserPreferencesDeleteDialogComponent>;
        let service: UserPreferencesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [UserPreferencesDeleteDialogComponent]
            })
                .overrideTemplate(UserPreferencesDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserPreferencesDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserPreferencesService);
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
