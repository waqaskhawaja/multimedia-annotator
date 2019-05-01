import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDataType } from 'app/shared/model/data-type.model';

@Component({
    selector: 'jhi-data-type-detail',
    templateUrl: './data-type-detail.component.html'
})
export class DataTypeDetailComponent implements OnInit {
    dataType: IDataType;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dataType }) => {
            this.dataType = dataType;
        });
    }

    previousState() {
        window.history.back();
    }
}
