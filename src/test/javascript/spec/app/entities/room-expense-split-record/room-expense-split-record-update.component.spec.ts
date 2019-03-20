/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomExpenseSplitRecordUpdateComponent } from 'app/entities/room-expense-split-record/room-expense-split-record-update.component';
import { RoomExpenseSplitRecordService } from 'app/entities/room-expense-split-record/room-expense-split-record.service';
import { RoomExpenseSplitRecord } from 'app/shared/model/room-expense-split-record.model';

describe('Component Tests', () => {
    describe('RoomExpenseSplitRecord Management Update Component', () => {
        let comp: RoomExpenseSplitRecordUpdateComponent;
        let fixture: ComponentFixture<RoomExpenseSplitRecordUpdateComponent>;
        let service: RoomExpenseSplitRecordService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomExpenseSplitRecordUpdateComponent]
            })
                .overrideTemplate(RoomExpenseSplitRecordUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoomExpenseSplitRecordUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomExpenseSplitRecordService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RoomExpenseSplitRecord(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.roomExpenseSplitRecord = entity;
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
                    const entity = new RoomExpenseSplitRecord();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.roomExpenseSplitRecord = entity;
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
