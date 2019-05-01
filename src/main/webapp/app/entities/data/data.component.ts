import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IData } from 'app/shared/model/data.model';
import { AccountService } from 'app/core';
import { DataService } from './data.service';

@Component({
    selector: 'jhi-data',
    templateUrl: './data.component.html'
})
export class DataComponent implements OnInit, OnDestroy {
    data: IData[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected dataService: DataService,
        protected jhiAlertService: JhiAlertService,
        protected dataUtils: JhiDataUtils,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.dataService
            .query()
            .pipe(
                filter((res: HttpResponse<IData[]>) => res.ok),
                map((res: HttpResponse<IData[]>) => res.body)
            )
            .subscribe(
                (res: IData[]) => {
                    this.data = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInData();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IData) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInData() {
        this.eventSubscriber = this.eventManager.subscribe('dataListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
