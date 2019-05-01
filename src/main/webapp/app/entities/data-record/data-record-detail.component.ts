import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDataRecord } from 'app/shared/model/data-record.model';

@Component({
    selector: 'jhi-data-record-detail',
    templateUrl: './data-record-detail.component.html'
})
export class DataRecordDetailComponent implements OnInit {
    dataRecord: IDataRecord;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dataRecord }) => {
            this.dataRecord = dataRecord;
        });
    }

    previousState() {
        window.history.back();
    }
}
