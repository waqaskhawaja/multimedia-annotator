import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISourceDataType } from 'app/shared/model/source-data-type.model';

@Component({
    selector: 'jhi-source-data-type-detail',
    templateUrl: './source-data-type-detail.component.html'
})
export class SourceDataTypeDetailComponent implements OnInit {
    sourceDataType: ISourceDataType;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sourceDataType }) => {
            this.sourceDataType = sourceDataType;
        });
    }

    previousState() {
        window.history.back();
    }
}
