import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IScenario } from 'app/shared/model/scenario.model';
import { AccountService } from 'app/core';
import { ScenarioService } from './scenario.service';

@Component({
    selector: 'jhi-scenario',
    templateUrl: './scenario.component.html'
})
export class ScenarioComponent implements OnInit, OnDestroy {
    scenarios: IScenario[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected scenarioService: ScenarioService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.scenarioService
            .query()
            .pipe(
                filter((res: HttpResponse<IScenario[]>) => res.ok),
                map((res: HttpResponse<IScenario[]>) => res.body)
            )
            .subscribe(
                (res: IScenario[]) => {
                    this.scenarios = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInScenarios();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IScenario) {
        return item.id;
    }

    registerChangeInScenarios() {
        this.eventSubscriber = this.eventManager.subscribe('scenarioListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
