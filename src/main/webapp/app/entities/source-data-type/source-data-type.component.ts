import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISourceDataType } from 'app/shared/model/source-data-type.model';
import { AccountService } from 'app/core';
import { SourceDataTypeService } from './source-data-type.service';

@Component({
    selector: 'jhi-source-data-type',
    templateUrl: './source-data-type.component.html'
})
export class SourceDataTypeComponent implements OnInit, OnDestroy {
    sourceDataTypes: ISourceDataType[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected sourceDataTypeService: SourceDataTypeService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.sourceDataTypeService
            .query()
            .pipe(
                filter((res: HttpResponse<ISourceDataType[]>) => res.ok),
                map((res: HttpResponse<ISourceDataType[]>) => res.body)
            )
            .subscribe(
                (res: ISourceDataType[]) => {
                    this.sourceDataTypes = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSourceDataTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISourceDataType) {
        return item.id;
    }

    registerChangeInSourceDataTypes() {
        this.eventSubscriber = this.eventManager.subscribe('sourceDataTypeListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
