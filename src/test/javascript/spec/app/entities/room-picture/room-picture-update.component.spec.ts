/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomPictureUpdateComponent } from 'app/entities/room-picture/room-picture-update.component';
import { RoomPictureService } from 'app/entities/room-picture/room-picture.service';
import { RoomPicture } from 'app/shared/model/room-picture.model';

describe('Component Tests', () => {
    describe('RoomPicture Management Update Component', () => {
        let comp: RoomPictureUpdateComponent;
        let fixture: ComponentFixture<RoomPictureUpdateComponent>;
        let service: RoomPictureService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomPictureUpdateComponent]
            })
                .overrideTemplate(RoomPictureUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoomPictureUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomPictureService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RoomPicture(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.roomPicture = entity;
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
                    const entity = new RoomPicture();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.roomPicture = entity;
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
