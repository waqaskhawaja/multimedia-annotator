import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MaSharedModule } from 'app/shared/shared.module';
import { AnalysisScenarioComponent } from './analysis-scenario.component';
import { AnalysisScenarioDetailComponent } from './analysis-scenario-detail.component';
import { AnalysisScenarioUpdateComponent } from './analysis-scenario-update.component';
import { AnalysisScenarioDeleteDialogComponent } from './analysis-scenario-delete-dialog.component';
import { analysisScenarioRoute } from './analysis-scenario.route';

@NgModule({
    imports: [MaSharedModule, FormsModule, ReactiveFormsModule, RouterModule.forChild(analysisScenarioRoute)],
    declarations: [
        AnalysisScenarioComponent,
        AnalysisScenarioDetailComponent,
        AnalysisScenarioUpdateComponent,
        AnalysisScenarioDeleteDialogComponent
    ],
    entryComponents: [AnalysisScenarioDeleteDialogComponent]
})
export class MaAnalysisScenarioModule {}
