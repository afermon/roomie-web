/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomFeatureUpdateComponent } from 'app/entities/room-feature/room-feature-update.component';
import { RoomFeatureService } from 'app/entities/room-feature/room-feature.service';
import { RoomFeature } from 'app/shared/model/room-feature.model';

describe('Component Tests', () => {
    describe('RoomFeature Management Update Component', () => {
        let comp: RoomFeatureUpdateComponent;
        let fixture: ComponentFixture<RoomFeatureUpdateComponent>;
        let service: RoomFeatureService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomFeatureUpdateComponent]
            })
                .overrideTemplate(RoomFeatureUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoomFeatureUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomFeatureService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoomFeature(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomFeature = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new RoomFeature();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomFeature = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
