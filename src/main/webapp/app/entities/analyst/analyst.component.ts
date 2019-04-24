import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAnalyst } from 'app/shared/model/analyst.model';
import { AccountService } from 'app/core';
import { AnalystService } from './analyst.service';

@Component({
    selector: 'jhi-analyst',
    templateUrl: './analyst.component.html'
})
export class AnalystComponent implements OnInit, OnDestroy {
    analysts: IAnalyst[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected analystService: AnalystService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.analystService
            .query()
            .pipe(
                filter((res: HttpResponse<IAnalyst[]>) => res.ok),
                map((res: HttpResponse<IAnalyst[]>) => res.body)
            )
            .subscribe(
                (res: IAnalyst[]) => {
                    this.analysts = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInAnalysts();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAnalyst) {
        return item.id;
    }

    registerChangeInAnalysts() {
        this.eventSubscriber = this.eventManager.subscribe('analystListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
