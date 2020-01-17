import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IInteractionRecord } from 'app/shared/model/interaction-record.model';
import { InteractionRecordService } from './interaction-record.service';
import { IInteractionType } from 'app/shared/model/interaction-type.model';
import { InteractionTypeService } from 'app/entities/interaction-type';
import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';
import { AnalysisSessionResourceService } from 'app/entities/analysis-session-resource';
import { IAnnotation } from 'app/shared/model/annotation.model';
import { AnnotationService } from 'app/entities/annotation';

@Component({
    selector: 'jhi-interaction-record-update',
    templateUrl: './interaction-record-update.component.html'
})
export class InteractionRecordUpdateComponent implements OnInit {
    interactionRecord: IInteractionRecord;
    isSaving: boolean;

    interactiontypes: IInteractionType[];

    analysissessionresources: IAnalysisSessionResource[];

    annotations: IAnnotation[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected interactionRecordService: InteractionRecordService,
        protected interactionTypeService: InteractionTypeService,
        protected analysisSessionResourceService: AnalysisSessionResourceService,
        protected annotationService: AnnotationService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ interactionRecord }) => {
            this.interactionRecord = interactionRecord;
        });
        this.interactionTypeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IInteractionType[]>) => mayBeOk.ok),
                map((response: HttpResponse<IInteractionType[]>) => response.body)
            )
            .subscribe((res: IInteractionType[]) => (this.interactiontypes = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.analysisSessionResourceService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAnalysisSessionResource[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAnalysisSessionResource[]>) => response.body)
            )
            .subscribe(
                (res: IAnalysisSessionResource[]) => (this.analysissessionresources = res),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.annotationService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAnnotation[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAnnotation[]>) => response.body)
            )
            .subscribe((res: IAnnotation[]) => (this.annotations = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.interactionRecord.id !== undefined) {
            this.subscribeToSaveResponse(this.interactionRecordService.update(this.interactionRecord));
        } else {
            this.subscribeToSaveResponse(this.interactionRecordService.create(this.interactionRecord));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IInteractionRecord>>) {
        result.subscribe((res: HttpResponse<IInteractionRecord>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackInteractionTypeById(index: number, item: IInteractionType) {
        return item.id;
    }

    trackAnalysisSessionResourceById(index: number, item: IAnalysisSessionResource) {
        return item.id;
    }

    trackAnnotationById(index: number, item: IAnnotation) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
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
