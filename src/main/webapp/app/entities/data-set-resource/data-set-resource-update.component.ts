import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IDataSetResource, DataSetResource } from 'app/shared/model/data-set-resource.model';
import { DataSetResourceService } from './data-set-resource.service';

@Component({
    selector: 'jhi-data-set-resource-update',
    templateUrl: './data-set-resource-update.component.html'
})
export class DataSetResourceUpdateComponent implements OnInit {
    isSaving: boolean;

    editForm = this.fb.group({
        id: [],
        name: [],
        sourceFile: [],
        sourceFileContentType: []
    });

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected dataSetResourceService: DataSetResourceService,
        protected activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ dataSetResource }) => {
            this.updateForm(dataSetResource);
        });
    }

    updateForm(dataSetResource: IDataSetResource) {
        this.editForm.patchValue({
            id: dataSetResource.id,
            name: dataSetResource.name,
            sourceFile: dataSetResource.sourceFile,
            sourceFileContentType: dataSetResource.sourceFileContentType
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
        const dataSetResource = this.createFromForm();
        if (dataSetResource.id !== undefined) {
            this.subscribeToSaveResponse(this.dataSetResourceService.update(dataSetResource));
        } else {
            this.subscribeToSaveResponse(this.dataSetResourceService.create(dataSetResource));
        }
    }

    private createFromForm(): IDataSetResource {
        return {
            ...new DataSetResource(),
            id: this.editForm.get(['id']).value,
            name: this.editForm.get(['name']).value,
            sourceFileContentType: this.editForm.get(['sourceFileContentType']).value,
            sourceFile: this.editForm.get(['sourceFile']).value
        };
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataSetResource>>) {
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
}
