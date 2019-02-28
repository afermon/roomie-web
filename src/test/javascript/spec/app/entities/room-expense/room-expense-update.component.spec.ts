/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomExpenseUpdateComponent } from 'app/entities/room-expense/room-expense-update.component';
import { RoomExpenseService } from 'app/entities/room-expense/room-expense.service';
import { RoomExpense } from 'app/shared/model/room-expense.model';

describe('Component Tests', () => {
    describe('RoomExpense Management Update Component', () => {
        let comp: RoomExpenseUpdateComponent;
        let fixture: ComponentFixture<RoomExpenseUpdateComponent>;
        let service: RoomExpenseService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomExpenseUpdateComponent]
            })
                .overrideTemplate(RoomExpenseUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoomExpenseUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomExpenseService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoomExpense(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomExpense = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoomExpense();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomExpense = entity;
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
