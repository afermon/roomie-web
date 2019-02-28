/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomTaskDetailComponent } from 'app/entities/room-task/room-task-detail.component';
import { RoomTask } from 'app/shared/model/room-task.model';

describe('Component Tests', () => {
    describe('RoomTask Management Detail Component', () => {
        let comp: RoomTaskDetailComponent;
        let fixture: ComponentFixture<RoomTaskDetailComponent>;
        const route = ({ data: of({ roomTask: new RoomTask(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomTaskDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RoomTaskDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RoomTaskDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.roomTask).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
