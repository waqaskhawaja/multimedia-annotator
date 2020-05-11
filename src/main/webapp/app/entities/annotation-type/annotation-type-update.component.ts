import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IAnnotationType, AnnotationType } from 'app/shared/model/annotation-type.model';
import { AnnotationTypeService } from './annotation-type.service';

@Component({
    selector: 'jhi-annotation-type-update',
    templateUrl: './annotation-type-update.component.html'
})
export class AnnotationTypeUpdateComponent implements OnInit {
    isSaving: boolean;

    editForm = this.fb.group({
        id: [],
        name: []
    });

    constructor(
        protected annotationTypeService: AnnotationTypeService,
        protected activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ annotationType }) => {
            this.updateForm(annotationType);
        });
    }

    updateForm(annotationType: IAnnotationType) {
        this.editForm.patchValue({
            id: annotationType.id,
            name: annotationType.name
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        const annotationType = this.createFromForm();
        if (annotationType.id !== undefined) {
            this.subscribeToSaveResponse(this.annotationTypeService.update(annotationType));
        } else {
            this.subscribeToSaveResponse(this.annotationTypeService.create(annotationType));
        }
    }

    private createFromForm(): IAnnotationType {
        return {
            ...new AnnotationType(),
            id: this.editForm.get(['id']).value,
            name: this.editForm.get(['name']).value
        };
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnotationType>>) {
        result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
