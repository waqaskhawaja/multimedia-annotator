import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IAnnotationType, AnnotationType } from 'app/shared/model/annotation-type.model';
import { AnnotationTypeService } from './annotation-type.service';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnnotationSessionService } from 'app/entities/annotation-session/annotation-session.service';

@Component({
    selector: 'jhi-annotation-type-update',
    templateUrl: './annotation-type-update.component.html'
})
export class AnnotationTypeUpdateComponent implements OnInit {
    isSaving: boolean;

    annotationsessions: IAnnotationSession[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected annotationTypeService: AnnotationTypeService,
        protected annotationSessionService: AnnotationSessionService,
        protected activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    editForm = this.fb.group({
        id: [],
        name: [],
        annotationSession: []
    });
    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ annotationType }) => {
            this.updateForm(annotationType);
        });
        this.annotationSessionService
            .query()
            .subscribe(
                (res: HttpResponse<IAnnotationSession[]>) => (this.annotationsessions = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    updateForm(annotationType: IAnnotationType) {
        this.editForm.patchValue({
            id: annotationType.id,
            name: annotationType.name,
            annotationSession: annotationType.annotationSession
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
            name: this.editForm.get(['name']).value,
            annotationSession: this.editForm.get(['annotationSession']).value
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
    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackAnnotationSessionById(index: number, item: IAnnotationSession) {
        return item.id;
    }
}
