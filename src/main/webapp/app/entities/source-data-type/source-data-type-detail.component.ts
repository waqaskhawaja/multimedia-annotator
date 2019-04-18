import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISourceDataType } from 'app/shared/model/source-data-type.model';

@Component({
    selector: 'jhi-source-data-type-detail',
    templateUrl: './source-data-type-detail.component.html'
})
export class SourceDataTypeDetailComponent implements OnInit {
    sourceDataType: ISourceDataType;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sourceDataType }) => {
            this.sourceDataType = sourceDataType;
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
