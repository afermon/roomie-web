/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomUpdateComponent } from 'app/entities/room/room-update.component';
import { RoomService } from 'app/entities/room/room.service';
import { Room } from 'app/shared/model/room.model';

describe('Component Tests', () => {
    describe('Room Management Update Component', () => {
        let comp: RoomUpdateComponent;
        let fixture: ComponentFixture<RoomUpdateComponent>;
        let service: RoomService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomUpdateComponent]
            })
                .overrideTemplate(RoomUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoomUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Room(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.room = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Room();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.room = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
