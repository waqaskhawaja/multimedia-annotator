import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnnotation } from 'app/shared/model/annotation.model';
import { AnnotationService } from './annotation.service';

@Component({
    selector: 'jhi-annotation-delete-dialog',
    templateUrl: './annotation-delete-dialog.component.html'
})
export class AnnotationDeleteDialogComponent {
    annotation: IAnnotation;

    constructor(
        protected annotationService: AnnotationService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.annotationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'annotationListModification',
                content: 'Deleted an annotation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-annotation-delete-popup',
    template: ''
})
export class AnnotationDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ annotation }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AnnotationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.annotation = annotation;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/annotation', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/annotation', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
