import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IDataSet } from 'app/shared/model/data-set.model';

@Component({
    selector: 'jhi-data-set-detail',
    templateUrl: './data-set-detail.component.html'
})
export class DataSetDetailComponent implements OnInit {
    dataSet: IDataSet;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dataSet }) => {
            this.dataSet = dataSet;
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
