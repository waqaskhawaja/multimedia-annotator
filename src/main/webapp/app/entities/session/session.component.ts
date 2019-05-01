import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ISession } from 'app/shared/model/session.model';
import { AccountService } from 'app/core';
import { SessionService } from './session.service';

@Component({
    selector: 'jhi-session',
    templateUrl: './session.component.html'
})
export class SessionComponent implements OnInit, OnDestroy {
    sessions: ISession[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected sessionService: SessionService,
        protected jhiAlertService: JhiAlertService,
        protected dataUtils: JhiDataUtils,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.sessionService
            .query()
            .pipe(
                filter((res: HttpResponse<ISession[]>) => res.ok),
                map((res: HttpResponse<ISession[]>) => res.body)
            )
            .subscribe(
                (res: ISession[]) => {
                    this.sessions = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSessions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISession) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInSessions() {
        this.eventSubscriber = this.eventManager.subscribe('sessionListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
