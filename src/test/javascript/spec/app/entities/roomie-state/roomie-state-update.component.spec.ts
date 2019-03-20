/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomieStateUpdateComponent } from 'app/entities/roomie-state/roomie-state-update.component';
import { RoomieStateService } from 'app/entities/roomie-state/roomie-state.service';
import { RoomieState } from 'app/shared/model/roomie-state.model';

describe('Component Tests', () => {
    describe('RoomieState Management Update Component', () => {
        let comp: RoomieStateUpdateComponent;
        let fixture: ComponentFixture<RoomieStateUpdateComponent>;
        let service: RoomieStateService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomieStateUpdateComponent]
            })
                .overrideTemplate(RoomieStateUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoomieStateUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomieStateService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoomieState(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomieState = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoomieState();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomieState = entity;
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
