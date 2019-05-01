import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDataType } from 'app/shared/model/data-type.model';
import { AccountService } from 'app/core';
import { DataTypeService } from './data-type.service';

@Component({
    selector: 'jhi-data-type',
    templateUrl: './data-type.component.html'
})
export class DataTypeComponent implements OnInit, OnDestroy {
    dataTypes: IDataType[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected dataTypeService: DataTypeService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.dataTypeService
            .query()
            .pipe(
                filter((res: HttpResponse<IDataType[]>) => res.ok),
                map((res: HttpResponse<IDataType[]>) => res.body)
            )
            .subscribe(
                (res: IDataType[]) => {
                    this.dataTypes = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDataTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDataType) {
        return item.id;
    }

    registerChangeInDataTypes() {
        this.eventSubscriber = this.eventManager.subscribe('dataTypeListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
