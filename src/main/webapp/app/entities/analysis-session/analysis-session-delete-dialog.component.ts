import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnalysisSession } from 'app/shared/model/analysis-session.model';
import { AnalysisSessionService } from './analysis-session.service';

@Component({
    selector: 'jhi-analysis-session-delete-dialog',
    templateUrl: './analysis-session-delete-dialog.component.html'
})
export class AnalysisSessionDeleteDialogComponent {
    analysisSession: IAnalysisSession;

    constructor(
        protected analysisSessionService: AnalysisSessionService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.analysisSessionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'analysisSessionListModification',
                content: 'Deleted an analysisSession'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-analysis-session-delete-popup',
    template: ''
})
export class AnalysisSessionDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ analysisSession }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AnalysisSessionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.analysisSession = analysisSession;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/analysis-session', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/analysis-session', { outlets: { popup: null } }]);
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
