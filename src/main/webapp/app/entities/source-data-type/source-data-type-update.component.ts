import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ISourceDataType } from 'app/shared/model/source-data-type.model';
import { SourceDataTypeService } from './source-data-type.service';
import { IScenario } from 'app/shared/model/scenario.model';
import { ScenarioService } from 'app/entities/scenario';

@Component({
    selector: 'jhi-source-data-type-update',
    templateUrl: './source-data-type-update.component.html'
})
export class SourceDataTypeUpdateComponent implements OnInit {
    sourceDataType: ISourceDataType;
    isSaving: boolean;

    scenarios: IScenario[];

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected sourceDataTypeService: SourceDataTypeService,
        protected scenarioService: ScenarioService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sourceDataType }) => {
            this.sourceDataType = sourceDataType;
        });
        this.scenarioService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IScenario[]>) => mayBeOk.ok),
                map((response: HttpResponse<IScenario[]>) => response.body)
            )
            .subscribe((res: IScenario[]) => (this.scenarios = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.sourceDataType.id !== undefined) {
            this.subscribeToSaveResponse(this.sourceDataTypeService.update(this.sourceDataType));
        } else {
            this.subscribeToSaveResponse(this.sourceDataTypeService.create(this.sourceDataType));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISourceDataType>>) {
        result.subscribe((res: HttpResponse<ISourceDataType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackScenarioById(index: number, item: IScenario) {
        return item.id;
    }
}
