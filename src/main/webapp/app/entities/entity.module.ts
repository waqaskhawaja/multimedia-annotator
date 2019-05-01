import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'data-type',
                loadChildren: './data-type/data-type.module#MultimediaAnnotatorDataTypeModule'
            },
            {
                path: 'interaction-type',
                loadChildren: './interaction-type/interaction-type.module#MultimediaAnnotatorInteractionTypeModule'
            },
            {
                path: 'scenario',
                loadChildren: './scenario/scenario.module#MultimediaAnnotatorScenarioModule'
            },
            {
                path: 'data',
                loadChildren: './data/data.module#MultimediaAnnotatorDataModule'
            },
            {
                path: 'data-record',
                loadChildren: './data-record/data-record.module#MultimediaAnnotatorDataRecordModule'
            },
            {
                path: 'analyst',
                loadChildren: './analyst/analyst.module#MultimediaAnnotatorAnalystModule'
            },
            {
                path: 'session',
                loadChildren: './session/session.module#MultimediaAnnotatorSessionModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MultimediaAnnotatorEntityModule {}
