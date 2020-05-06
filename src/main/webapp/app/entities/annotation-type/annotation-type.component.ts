import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnnotationType } from 'app/shared/model/annotation-type.model';
import { AnnotationTypeService } from './annotation-type.service';
import { AnnotationTypeDeleteDialogComponent } from './annotation-type-delete-dialog.component';

@Component({
    selector: 'jhi-annotation-type',
    templateUrl: './annotation-type.component.html'
})
export class AnnotationTypeComponent implements OnInit, OnDestroy {
    annotationTypes: IAnnotationType[];
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected annotationTypeService: AnnotationTypeService,
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
            this.annotationTypeService
                .search({
                    query: this.currentSearch
                })
                .subscribe((res: HttpResponse<IAnnotationType[]>) => (this.annotationTypes = res.body));
            return;
        }
        this.annotationTypeService.query().subscribe((res: HttpResponse<IAnnotationType[]>) => {
            this.annotationTypes = res.body;
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
        this.registerChangeInAnnotationTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAnnotationType) {
        return item.id;
    }

    registerChangeInAnnotationTypes() {
        this.eventSubscriber = this.eventManager.subscribe('annotationTypeListModification', () => this.loadAll());
    }

    delete(annotationType: IAnnotationType) {
        const modalRef = this.modalService.open(AnnotationTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.annotationType = annotationType;
    }
}
