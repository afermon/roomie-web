/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomieStateDetailComponent } from 'app/entities/roomie-state/roomie-state-detail.component';
import { RoomieState } from 'app/shared/model/roomie-state.model';

describe('Component Tests', () => {
    describe('RoomieState Management Detail Component', () => {
        let comp: RoomieStateDetailComponent;
        let fixture: ComponentFixture<RoomieStateDetailComponent>;
        const route = ({ data: of({ roomieState: new RoomieState(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomieStateDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RoomieStateDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomieStateDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.roomieState).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
