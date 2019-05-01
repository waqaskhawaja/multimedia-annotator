import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IData } from 'app/shared/model/data.model';
import { DataService } from './data.service';
import { IDataType } from 'app/shared/model/data-type.model';
import { DataTypeService } from 'app/entities/data-type';

@Component({
    selector: 'jhi-data-update',
    templateUrl: './data-update.component.html'
})
export class DataUpdateComponent implements OnInit {
    data: IData;
    isSaving: boolean;

    datatypes: IDataType[];

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected dataService: DataService,
        protected dataTypeService: DataTypeService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ data }) => {
            this.data = data;
        });
        this.dataTypeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDataType[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDataType[]>) => response.body)
            )
            .subscribe((res: IDataType[]) => (this.datatypes = res), (res: HttpErrorResponse) => this.onError(res.message));
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
        if (this.data.id !== undefined) {
            this.subscribeToSaveResponse(this.dataService.update(this.data));
        } else {
            this.subscribeToSaveResponse(this.dataService.create(this.data));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IData>>) {
        result.subscribe((res: HttpResponse<IData>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackDataTypeById(index: number, item: IDataType) {
        return item.id;
    }
}
