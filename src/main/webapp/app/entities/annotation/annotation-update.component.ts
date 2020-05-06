import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IAnnotation, Annotation } from 'app/shared/model/annotation.model';
import { AnnotationService } from './annotation.service';
import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnnotationSessionService } from 'app/entities/annotation-session/annotation-session.service';
import { IInteractionRecord } from 'app/shared/model/interaction-record.model';
import { InteractionRecordService } from 'app/entities/interaction-record/interaction-record.service';
import { IAnnotationType } from 'app/shared/model/annotation-type.model';
import { AnnotationTypeService } from 'app/entities/annotation-type/annotation-type.service';

@Component({
    selector: 'jhi-annotation-update',
    templateUrl: './annotation-update.component.html'
})
export class AnnotationUpdateComponent implements OnInit {
    isSaving: boolean;

    annotationsessions: IAnnotationSession[];

    interactionrecords: IInteractionRecord[];

    annotationtypes: IAnnotationType[];

    editForm = this.fb.group({
        id: [],
        start: [],
        end: [],
        annotationText: [],
        annotationSession: [],
        interactionRecords: [],
        annotationType: []
    });

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected annotationService: AnnotationService,
        protected annotationSessionService: AnnotationSessionService,
        protected interactionRecordService: InteractionRecordService,
        protected annotationTypeService: AnnotationTypeService,
        protected activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ annotation }) => {
            this.updateForm(annotation);
        });
        this.annotationSessionService
            .query()
            .subscribe(
                (res: HttpResponse<IAnnotationSession[]>) => (this.annotationsessions = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.interactionRecordService
            .query()
            .subscribe(
                (res: HttpResponse<IInteractionRecord[]>) => (this.interactionrecords = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.annotationTypeService
            .query()
            .subscribe(
                (res: HttpResponse<IAnnotationType[]>) => (this.annotationtypes = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    updateForm(annotation: IAnnotation) {
        this.editForm.patchValue({
            id: annotation.id,
            start: annotation.start != null ? annotation.start.format(DATE_TIME_FORMAT) : null,
            end: annotation.end != null ? annotation.end.format(DATE_TIME_FORMAT) : null,
            annotationText: annotation.annotationText,
            annotationSession: annotation.annotationSession,
            interactionRecords: annotation.interactionRecords,
            annotationType: annotation.annotationType
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        const annotation = this.createFromForm();
        if (annotation.id !== undefined) {
            this.subscribeToSaveResponse(this.annotationService.update(annotation));
        } else {
            this.subscribeToSaveResponse(this.annotationService.create(annotation));
        }
    }

    private createFromForm(): IAnnotation {
        return {
            ...new Annotation(),
            id: this.editForm.get(['id']).value,
            start: this.editForm.get(['start']).value != null ? moment(this.editForm.get(['start']).value, DATE_TIME_FORMAT) : undefined,
            end: this.editForm.get(['end']).value != null ? moment(this.editForm.get(['end']).value, DATE_TIME_FORMAT) : undefined,
            annotationText: this.editForm.get(['annotationText']).value,
            annotationSession: this.editForm.get(['annotationSession']).value,
            interactionRecords: this.editForm.get(['interactionRecords']).value,
            annotationType: this.editForm.get(['annotationType']).value
        };
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnotation>>) {
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

    trackInteractionRecordById(index: number, item: IInteractionRecord) {
        return item.id;
    }

    trackAnnotationTypeById(index: number, item: IAnnotationType) {
        return item.id;
    }

    getSelected(selectedVals: any[], option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
