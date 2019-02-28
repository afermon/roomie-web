import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IUserPreferences } from 'app/shared/model/user-preferences.model';
import { UserPreferencesService } from './user-preferences.service';

@Component({
    selector: 'jhi-user-preferences-update',
    templateUrl: './user-preferences-update.component.html'
})
export class UserPreferencesUpdateComponent implements OnInit {
    userPreferences: IUserPreferences;
    isSaving: boolean;

    constructor(protected userPreferencesService: UserPreferencesService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userPreferences }) => {
            this.userPreferences = userPreferences;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.userPreferences.id !== undefined) {
            this.subscribeToSaveResponse(this.userPreferencesService.update(this.userPreferences));
        } else {
            this.subscribeToSaveResponse(this.userPreferencesService.create(this.userPreferences));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserPreferences>>) {
        result.subscribe((res: HttpResponse<IUserPreferences>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
