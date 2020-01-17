import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';
import { AnalysisScenarioService } from './analysis-scenario.service';

@Component({
    selector: 'jhi-analysis-scenario-delete-dialog',
    templateUrl: './analysis-scenario-delete-dialog.component.html'
})
export class AnalysisScenarioDeleteDialogComponent {
    analysisScenario: IAnalysisScenario;

    constructor(
        protected analysisScenarioService: AnalysisScenarioService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.analysisScenarioService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'analysisScenarioListModification',
                content: 'Deleted an analysisScenario'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-analysis-scenario-delete-popup',
    template: ''
})
export class AnalysisScenarioDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ analysisScenario }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AnalysisScenarioDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.analysisScenario = analysisScenario;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/analysis-scenario', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/analysis-scenario', { outlets: { popup: null } }]);
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
