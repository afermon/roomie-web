/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomFeatureDetailComponent } from 'app/entities/room-feature/room-feature-detail.component';
import { RoomFeature } from 'app/shared/model/room-feature.model';

describe('Component Tests', () => {
    describe('RoomFeature Management Detail Component', () => {
        let comp: RoomFeatureDetailComponent;
        let fixture: ComponentFixture<RoomFeatureDetailComponent>;
        const route = ({ data: of({ roomFeature: new RoomFeature(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomFeatureDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RoomFeatureDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomFeatureDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.roomFeature).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
