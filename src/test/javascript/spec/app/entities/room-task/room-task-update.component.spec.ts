/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomTaskUpdateComponent } from 'app/entities/room-task/room-task-update.component';
import { RoomTaskService } from 'app/entities/room-task/room-task.service';
import { RoomTask } from 'app/shared/model/room-task.model';

describe('Component Tests', () => {
    describe('RoomTask Management Update Component', () => {
        let comp: RoomTaskUpdateComponent;
        let fixture: ComponentFixture<RoomTaskUpdateComponent>;
        let service: RoomTaskService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomTaskUpdateComponent]
            })
                .overrideTemplate(RoomTaskUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoomTaskUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomTaskService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RoomTask(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.roomTask = entity;
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
                    const entity = new RoomTask();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.roomTask = entity;
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
