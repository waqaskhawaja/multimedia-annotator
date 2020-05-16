import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';
import { AnalysisScenarioService } from './analysis-scenario.service';

@Component({
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
        this.analysisScenarioService.delete(id).subscribe(() => {
            this.eventManager.broadcast({
                name: 'analysisScenarioListModification',
                content: 'Deleted an analysisScenario'
            });
            this.activeModal.dismiss(true);
        });
    }
}
