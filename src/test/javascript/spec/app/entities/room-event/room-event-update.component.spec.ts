/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomEventUpdateComponent } from 'app/entities/room-event/room-event-update.component';
import { RoomEventService } from 'app/entities/room-event/room-event.service';
import { RoomEvent } from 'app/shared/model/room-event.model';

describe('Component Tests', () => {
    describe('RoomEvent Management Update Component', () => {
        let comp: RoomEventUpdateComponent;
        let fixture: ComponentFixture<RoomEventUpdateComponent>;
        let service: RoomEventService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomEventUpdateComponent]
            })
                .overrideTemplate(RoomEventUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoomEventUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomEventService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoomEvent(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomEvent = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoomEvent();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomEvent = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
