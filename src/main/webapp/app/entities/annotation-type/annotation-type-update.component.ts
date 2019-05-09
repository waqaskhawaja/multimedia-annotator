import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAnnotationType } from 'app/shared/model/annotation-type.model';
import { AnnotationTypeService } from './annotation-type.service';

@Component({
    selector: 'jhi-annotation-type-update',
    templateUrl: './annotation-type-update.component.html'
})
export class AnnotationTypeUpdateComponent implements OnInit {
    annotationType: IAnnotationType;
    isSaving: boolean;

    annotationtypes: IAnnotationType[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected annotationTypeService: AnnotationTypeService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ annotationType }) => {
            this.annotationType = annotationType;
        });
        this.annotationTypeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAnnotationType[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAnnotationType[]>) => response.body)
            )
            .subscribe((res: IAnnotationType[]) => (this.annotationtypes = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.annotationType.id !== undefined) {
            this.subscribeToSaveResponse(this.annotationTypeService.update(this.annotationType));
        } else {
            this.subscribeToSaveResponse(this.annotationTypeService.create(this.annotationType));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnotationType>>) {
        result.subscribe((res: HttpResponse<IAnnotationType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAnnotationTypeById(index: number, item: IAnnotationType) {
        return item.id;
    }
}
