import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IDataSet, DataSet } from 'app/shared/model/data-set.model';
import { DataSetService } from './data-set.service';
import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';
import { AnalysisScenarioService } from 'app/entities/analysis-scenario/analysis-scenario.service';

@Component({
    selector: 'jhi-data-set-update',
    templateUrl: './data-set-update.component.html'
})
export class DataSetUpdateComponent implements OnInit {
    isSaving: boolean;

    analysisscenarios: IAnalysisScenario[];

    editForm = this.fb.group({
        id: [],
        title: [],
        date: [],
        type: [],
        contents: [],
        identifier: [],
        analysisScenario: []
    });

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected dataSetService: DataSetService,
        protected analysisScenarioService: AnalysisScenarioService,
        protected activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ dataSet }) => {
            this.updateForm(dataSet);
        });
        this.analysisScenarioService
            .query()
            .subscribe(
                (res: HttpResponse<IAnalysisScenario[]>) => (this.analysisscenarios = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    updateForm(dataSet: IDataSet) {
        this.editForm.patchValue({
            id: dataSet.id,
            title: dataSet.title,
            date: dataSet.date != null ? dataSet.date.format(DATE_TIME_FORMAT) : null,
            type: dataSet.type,
            contents: dataSet.contents,
            identifier: dataSet.identifier,
            analysisScenario: dataSet.analysisScenario
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, field: string, isImage) {
        return new Promise((resolve, reject) => {
            if (event && event.target && event.target.files && event.target.files[0]) {
                const file: File = event.target.files[0];
                if (isImage && !file.type.startsWith('image/')) {
                    reject(`File was expected to be an image but was found to be ${file.type}`);
                } else {
                    const filedContentType: string = field + 'ContentType';
                    this.dataUtils.toBase64(file, base64Data => {
                        this.editForm.patchValue({
                            [field]: base64Data,
                            [filedContentType]: file.type
                        });
                    });
                }
            } else {
                reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
            }
        }).then(
            // eslint-disable-next-line no-console
            () => console.log('blob added'), // success
            this.onError
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        const dataSet = this.createFromForm();
        if (dataSet.id !== undefined) {
            this.subscribeToSaveResponse(this.dataSetService.update(dataSet));
        } else {
            this.subscribeToSaveResponse(this.dataSetService.create(dataSet));
        }
    }

    private createFromForm(): IDataSet {
        return {
            ...new DataSet(),
            id: this.editForm.get(['id']).value,
            title: this.editForm.get(['title']).value,
            date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined,
            type: this.editForm.get(['type']).value,
            contents: this.editForm.get(['contents']).value,
            identifier: this.editForm.get(['identifier']).value,
            analysisScenario: this.editForm.get(['analysisScenario']).value
        };
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataSet>>) {
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

    trackAnalysisScenarioById(index: number, item: IAnalysisScenario) {
        return item.id;
    }
}
