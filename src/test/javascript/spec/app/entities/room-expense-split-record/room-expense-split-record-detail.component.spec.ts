/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomExpenseSplitRecordDetailComponent } from 'app/entities/room-expense-split-record/room-expense-split-record-detail.component';
import { RoomExpenseSplitRecord } from 'app/shared/model/room-expense-split-record.model';

describe('Component Tests', () => {
    describe('RoomExpenseSplitRecord Management Detail Component', () => {
        let comp: RoomExpenseSplitRecordDetailComponent;
        let fixture: ComponentFixture<RoomExpenseSplitRecordDetailComponent>;
        const route = ({ data: of({ roomExpenseSplitRecord: new RoomExpenseSplitRecord(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomExpenseSplitRecordDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RoomExpenseSplitRecordDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomExpenseSplitRecordDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.roomExpenseSplitRecord).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
