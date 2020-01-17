import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IResourceType } from 'app/shared/model/resource-type.model';
import { ResourceTypeService } from './resource-type.service';

@Component({
    selector: 'jhi-resource-type-update',
    templateUrl: './resource-type-update.component.html'
})
export class ResourceTypeUpdateComponent implements OnInit {
    resourceType: IResourceType;
    isSaving: boolean;

    constructor(protected resourceTypeService: ResourceTypeService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ resourceType }) => {
            this.resourceType = resourceType;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.resourceType.id !== undefined) {
            this.subscribeToSaveResponse(this.resourceTypeService.update(this.resourceType));
        } else {
            this.subscribeToSaveResponse(this.resourceTypeService.create(this.resourceType));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourceType>>) {
        result.subscribe((res: HttpResponse<IResourceType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
