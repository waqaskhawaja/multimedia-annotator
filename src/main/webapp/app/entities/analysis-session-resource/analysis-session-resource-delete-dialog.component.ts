import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';
import { AnalysisSessionResourceService } from './analysis-session-resource.service';

@Component({
    selector: 'jhi-analysis-session-resource-delete-dialog',
    templateUrl: './analysis-session-resource-delete-dialog.component.html'
})
export class AnalysisSessionResourceDeleteDialogComponent {
    analysisSessionResource: IAnalysisSessionResource;

    constructor(
        protected analysisSessionResourceService: AnalysisSessionResourceService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.analysisSessionResourceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'analysisSessionResourceListModification',
                content: 'Deleted an analysisSessionResource'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-analysis-session-resource-delete-popup',
    template: ''
})
export class AnalysisSessionResourceDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ analysisSessionResource }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AnalysisSessionResourceDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.analysisSessionResource = analysisSessionResource;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/analysis-session-resource', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/analysis-session-resource', { outlets: { popup: null } }]);
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
