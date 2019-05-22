import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';
import { AnalysisSessionResourceService } from './analysis-session-resource.service';
import { IResourceType } from 'app/shared/model/resource-type.model';
import { ResourceTypeService } from 'app/entities/resource-type';
import { IAnalysisSession } from 'app/shared/model/analysis-session.model';
import { AnalysisSessionService } from 'app/entities/analysis-session';

@Component({
    selector: 'jhi-analysis-session-resource-update',
    templateUrl: './analysis-session-resource-update.component.html'
})
export class AnalysisSessionResourceUpdateComponent implements OnInit {
    analysisSessionResource: IAnalysisSessionResource;
    isSaving: boolean;

    resourcetypes: IResourceType[];

    analysissessions: IAnalysisSession[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected analysisSessionResourceService: AnalysisSessionResourceService,
        protected resourceTypeService: ResourceTypeService,
        protected analysisSessionService: AnalysisSessionService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ analysisSessionResource }) => {
            this.analysisSessionResource = analysisSessionResource;
        });
        this.resourceTypeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IResourceType[]>) => mayBeOk.ok),
                map((response: HttpResponse<IResourceType[]>) => response.body)
            )
            .subscribe((res: IResourceType[]) => (this.resourcetypes = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.analysisSessionService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAnalysisSession[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAnalysisSession[]>) => response.body)
            )
            .subscribe((res: IAnalysisSession[]) => (this.analysissessions = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.analysisSessionResource.id !== undefined) {
            this.subscribeToSaveResponse(this.analysisSessionResourceService.update(this.analysisSessionResource));
        } else {
            this.subscribeToSaveResponse(this.analysisSessionResourceService.create(this.analysisSessionResource));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnalysisSessionResource>>) {
        result.subscribe(
            (res: HttpResponse<IAnalysisSessionResource>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
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

    trackResourceTypeById(index: number, item: IResourceType) {
        return item.id;
    }

    trackAnalysisSessionById(index: number, item: IAnalysisSession) {
        return item.id;
    }
}
