import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDataSet } from 'app/shared/model/data-set.model';
import { DataSetService } from './data-set.service';
import { DataSetDeleteDialogComponent } from './data-set-delete-dialog.component';

@Component({
    selector: 'jhi-data-set',
    templateUrl: './data-set.component.html'
})
export class DataSetComponent implements OnInit, OnDestroy {
    dataSets: IDataSet[];
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected dataSetService: DataSetService,
        protected dataUtils: JhiDataUtils,
        protected eventManager: JhiEventManager,
        protected modalService: NgbModal,
        protected activatedRoute: ActivatedRoute
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
                ? this.activatedRoute.snapshot.queryParams['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.dataSetService
                .search({
                    query: this.currentSearch
                })
                .subscribe((res: HttpResponse<IDataSet[]>) => (this.dataSets = res.body));
            return;
        }
        this.dataSetService.query().subscribe((res: HttpResponse<IDataSet[]>) => {
            this.dataSets = res.body;
            this.currentSearch = '';
        });
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.registerChangeInDataSets();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDataSet) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInDataSets() {
        this.eventSubscriber = this.eventManager.subscribe('dataSetListModification', () => this.loadAll());
    }

    delete(dataSet: IDataSet) {
        const modalRef = this.modalService.open(DataSetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.dataSet = dataSet;
    }
}
