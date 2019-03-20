/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { RoomieUpdateComponent } from 'app/entities/roomie/roomie-update.component';
import { RoomieService } from 'app/entities/roomie/roomie.service';
import { Roomie } from 'app/shared/model/roomie.model';

describe('Component Tests', () => {
    describe('Roomie Management Update Component', () => {
        let comp: RoomieUpdateComponent;
        let fixture: ComponentFixture<RoomieUpdateComponent>;
        let service: RoomieService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [RoomieUpdateComponent]
            })
                .overrideTemplate(RoomieUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RoomieUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RoomieService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Roomie(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomie = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Roomie();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.roomie = entity;
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
