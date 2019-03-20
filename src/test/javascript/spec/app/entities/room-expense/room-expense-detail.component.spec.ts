/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomExpenseDetailComponent } from 'app/entities/room-expense/room-expense-detail.component';
import { RoomExpense } from 'app/shared/model/room-expense.model';

describe('Component Tests', () => {
    describe('RoomExpense Management Detail Component', () => {
        let comp: RoomExpenseDetailComponent;
        let fixture: ComponentFixture<RoomExpenseDetailComponent>;
        const route = ({ data: of({ roomExpense: new RoomExpense(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomExpenseDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RoomExpenseDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomExpenseDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.roomExpense).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
