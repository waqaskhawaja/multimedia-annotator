import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IDataType } from 'app/shared/model/data-type.model';
import { DataTypeService } from './data-type.service';

@Component({
    selector: 'jhi-data-type-update',
    templateUrl: './data-type-update.component.html'
})
export class DataTypeUpdateComponent implements OnInit {
    dataType: IDataType;
    isSaving: boolean;

    constructor(protected dataTypeService: DataTypeService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ dataType }) => {
            this.dataType = dataType;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.dataType.id !== undefined) {
            this.subscribeToSaveResponse(this.dataTypeService.update(this.dataType));
        } else {
            this.subscribeToSaveResponse(this.dataTypeService.create(this.dataType));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataType>>) {
        result.subscribe((res: HttpResponse<IDataType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
