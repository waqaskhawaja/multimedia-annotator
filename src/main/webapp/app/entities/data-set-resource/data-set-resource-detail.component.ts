import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IDataSetResource } from 'app/shared/model/data-set-resource.model';

@Component({
    selector: 'jhi-data-set-resource-detail',
    templateUrl: './data-set-resource-detail.component.html'
})
export class DataSetResourceDetailComponent implements OnInit {
    dataSetResource: IDataSetResource;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dataSetResource }) => {
            this.dataSetResource = dataSetResource;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
