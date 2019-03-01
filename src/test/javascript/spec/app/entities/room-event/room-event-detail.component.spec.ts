/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomEventDetailComponent } from 'app/entities/room-event/room-event-detail.component';
import { RoomEvent } from 'app/shared/model/room-event.model';

describe('Component Tests', () => {
    describe('RoomEvent Management Detail Component', () => {
        let comp: RoomEventDetailComponent;
        let fixture: ComponentFixture<RoomEventDetailComponent>;
        const route = ({ data: of({ roomEvent: new RoomEvent(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomEventDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RoomEventDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomEventDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.roomEvent).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
