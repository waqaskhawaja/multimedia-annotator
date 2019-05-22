import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnnotationSession } from 'app/shared/model/annotation-session.model';
import { AnnotationSessionService } from './annotation-session.service';

@Component({
    selector: 'jhi-annotation-session-delete-dialog',
    templateUrl: './annotation-session-delete-dialog.component.html'
})
export class AnnotationSessionDeleteDialogComponent {
    annotationSession: IAnnotationSession;

    constructor(
        protected annotationSessionService: AnnotationSessionService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.annotationSessionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'annotationSessionListModification',
                content: 'Deleted an annotationSession'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-annotation-session-delete-popup',
    template: ''
})
export class AnnotationSessionDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ annotationSession }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AnnotationSessionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.annotationSession = annotationSession;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/annotation-session', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/annotation-session', { outlets: { popup: null } }]);
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
