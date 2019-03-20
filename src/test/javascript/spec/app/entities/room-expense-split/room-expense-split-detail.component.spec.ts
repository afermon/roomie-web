/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomExpenseSplitDetailComponent } from 'app/entities/room-expense-split/room-expense-split-detail.component';
import { RoomExpenseSplit } from 'app/shared/model/room-expense-split.model';

describe('Component Tests', () => {
    describe('RoomExpenseSplit Management Detail Component', () => {
        let comp: RoomExpenseSplitDetailComponent;
        let fixture: ComponentFixture<RoomExpenseSplitDetailComponent>;
        const route = ({ data: of({ roomExpenseSplit: new RoomExpenseSplit(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomExpenseSplitDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RoomExpenseSplitDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomExpenseSplitDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.roomExpenseSplit).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
