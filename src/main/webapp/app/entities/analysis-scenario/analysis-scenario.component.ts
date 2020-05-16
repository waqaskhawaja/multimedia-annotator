import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AnalysisScenarioService } from './analysis-scenario.service';
import { AnalysisScenarioDeleteDialogComponent } from './analysis-scenario-delete-dialog.component';

@Component({
    selector: 'jhi-analysis-scenario',
    templateUrl: './analysis-scenario.component.html'
})
export class AnalysisScenarioComponent implements OnInit, OnDestroy {
    analysisScenarios: IAnalysisScenario[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        protected analysisScenarioService: AnalysisScenarioService,
        protected parseLinks: JhiParseLinks,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected eventManager: JhiEventManager,
        protected modalService: NgbModal
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
                ? this.activatedRoute.snapshot.queryParams['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.analysisScenarioService
                .search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe((res: HttpResponse<IAnalysisScenario[]>) => this.paginateAnalysisScenarios(res.body, res.headers));
            return;
        }
        this.analysisScenarioService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe((res: HttpResponse<IAnalysisScenario[]>) => this.paginateAnalysisScenarios(res.body, res.headers));
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/analysis-scenario'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            '/analysis-scenario',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            '/analysis-scenario',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.registerChangeInAnalysisScenarios();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAnalysisScenario) {
        return item.id;
    }

    registerChangeInAnalysisScenarios() {
        this.eventSubscriber = this.eventManager.subscribe('analysisScenarioListModification', () => this.loadAll());
    }

    delete(analysisScenario: IAnalysisScenario) {
        const modalRef = this.modalService.open(AnalysisScenarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.analysisScenario = analysisScenario;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateAnalysisScenarios(data: IAnalysisScenario[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.analysisScenarios = data;
    }
}
