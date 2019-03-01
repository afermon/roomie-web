/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomPictureDetailComponent } from 'app/entities/room-picture/room-picture-detail.component';
import { RoomPicture } from 'app/shared/model/room-picture.model';

describe('Component Tests', () => {
    describe('RoomPicture Management Detail Component', () => {
        let comp: RoomPictureDetailComponent;
        let fixture: ComponentFixture<RoomPictureDetailComponent>;
        const route = ({ data: of({ roomPicture: new RoomPicture(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomPictureDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RoomPictureDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomPictureDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.roomPicture).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
