import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnnotationType } from 'app/shared/model/annotation-type.model';
import { AnnotationTypeService } from './annotation-type.service';

@Component({
    selector: 'jhi-annotation-type-delete-dialog',
    templateUrl: './annotation-type-delete-dialog.component.html'
})
export class AnnotationTypeDeleteDialogComponent {
    annotationType: IAnnotationType;

    constructor(
        protected annotationTypeService: AnnotationTypeService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.annotationTypeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'annotationTypeListModification',
                content: 'Deleted an annotationType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-annotation-type-delete-popup',
    template: ''
})
export class AnnotationTypeDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ annotationType }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AnnotationTypeDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.annotationType = annotationType;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/annotation-type', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/annotation-type', { outlets: { popup: null } }]);
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
