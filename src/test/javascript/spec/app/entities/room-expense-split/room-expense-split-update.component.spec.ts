/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomExpenseSplitUpdateComponent } from 'app/entities/room-expense-split/room-expense-split-update.component';
import { RoomExpenseSplitService } from 'app/entities/room-expense-split/room-expense-split.service';
import { RoomExpenseSplit } from 'app/shared/model/room-expense-split.model';

describe('Component Tests', () => {
    describe('RoomExpenseSplit Management Update Component', () => {
        let comp: RoomExpenseSplitUpdateComponent;
        let fixture: ComponentFixture<RoomExpenseSplitUpdateComponent>;
        let service: RoomExpenseSplitService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomExpenseSplitUpdateComponent]
            })
                .overrideTemplate(RoomExpenseSplitUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoomExpenseSplitUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomExpenseSplitService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoomExpenseSplit(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomExpenseSplit = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoomExpenseSplit();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomExpenseSplit = entity;
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
