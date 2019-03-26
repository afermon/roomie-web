/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RoomieTestModule } from '../../../test.module';
import { UserPreferencesUpdateComponent } from 'app/entities/user-preferences/user-preferences-update.component';
import { UserPreferencesService } from 'app/entities/user-preferences/user-preferences.service';
import { UserPreferences } from 'app/shared/model/user-preferences.model';

describe('Component Tests', () => {
    describe('UserPreferences Management Update Component', () => {
        let comp: UserPreferencesUpdateComponent;
        let fixture: ComponentFixture<UserPreferencesUpdateComponent>;
        let service: UserPreferencesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RoomieTestModule],
                declarations: [UserPreferencesUpdateComponent]
            })
                .overrideTemplate(UserPreferencesUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserPreferencesUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserPreferencesService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new UserPreferences(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.userPreferences = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new UserPreferences();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.userPreferences = entity;
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
