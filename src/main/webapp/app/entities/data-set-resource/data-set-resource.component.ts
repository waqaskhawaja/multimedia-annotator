import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDataSetResource } from 'app/shared/model/data-set-resource.model';
import { DataSetResourceService } from './data-set-resource.service';
import { DataSetResourceDeleteDialogComponent } from './data-set-resource-delete-dialog.component';

@Component({
    selector: 'jhi-data-set-resource',
    templateUrl: './data-set-resource.component.html'
})
export class DataSetResourceComponent implements OnInit, OnDestroy {
    dataSetResources: IDataSetResource[];
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected dataSetResourceService: DataSetResourceService,
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
            this.dataSetResourceService
                .search({
                    query: this.currentSearch
                })
                .subscribe((res: HttpResponse<IDataSetResource[]>) => (this.dataSetResources = res.body));
            return;
        }
        this.dataSetResourceService.query().subscribe((res: HttpResponse<IDataSetResource[]>) => {
            this.dataSetResources = res.body;
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
        this.registerChangeInDataSetResources();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDataSetResource) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInDataSetResources() {
        this.eventSubscriber = this.eventManager.subscribe('dataSetResourceListModification', () => this.loadAll());
    }

    delete(dataSetResource: IDataSetResource) {
        const modalRef = this.modalService.open(DataSetResourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.dataSetResource = dataSetResource;
    }
}
