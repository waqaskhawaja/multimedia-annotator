import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDataRecord } from 'app/shared/model/data-record.model';
import { AccountService } from 'app/core';
import { DataRecordService } from './data-record.service';

@Component({
    selector: 'jhi-data-record',
    templateUrl: './data-record.component.html'
})
export class DataRecordComponent implements OnInit, OnDestroy {
    dataRecords: IDataRecord[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected dataRecordService: DataRecordService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.dataRecordService
            .query()
            .pipe(
                filter((res: HttpResponse<IDataRecord[]>) => res.ok),
                map((res: HttpResponse<IDataRecord[]>) => res.body)
            )
            .subscribe(
                (res: IDataRecord[]) => {
                    this.dataRecords = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDataRecords();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDataRecord) {
        return item.id;
    }

    registerChangeInDataRecords() {
        this.eventSubscriber = this.eventManager.subscribe('dataRecordListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
