/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomieDetailComponent } from 'app/entities/roomie/roomie-detail.component';
import { Roomie } from 'app/shared/model/roomie.model';

describe('Component Tests', () => {
    describe('Roomie Management Detail Component', () => {
        let comp: RoomieDetailComponent;
        let fixture: ComponentFixture<RoomieDetailComponent>;
        const route = ({ data: of({ roomie: new Roomie(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomieDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RoomieDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomieDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.roomie).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
