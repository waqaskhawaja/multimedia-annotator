import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IInteractionType } from 'app/shared/model/interaction-type.model';
import { AccountService } from 'app/core';
import { InteractionTypeService } from './interaction-type.service';

@Component({
    selector: 'jhi-interaction-type',
    templateUrl: './interaction-type.component.html'
})
export class InteractionTypeComponent implements OnInit, OnDestroy {
    interactionTypes: IInteractionType[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected interactionTypeService: InteractionTypeService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.interactionTypeService
            .query()
            .pipe(
                filter((res: HttpResponse<IInteractionType[]>) => res.ok),
                map((res: HttpResponse<IInteractionType[]>) => res.body)
            )
            .subscribe(
                (res: IInteractionType[]) => {
                    this.interactionTypes = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInInteractionTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IInteractionType) {
        return item.id;
    }

    registerChangeInInteractionTypes() {
        this.eventSubscriber = this.eventManager.subscribe('interactionTypeListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
