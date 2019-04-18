import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'scenario',
                loadChildren: './scenario/scenario.module#MultimediaAnnotatorScenarioModule'
            },
            {
                path: 'source-data-type',
                loadChildren: './source-data-type/source-data-type.module#MultimediaAnnotatorSourceDataTypeModule'
            },
            {
                path: 'source-data-type',
                loadChildren: './source-data-type/source-data-type.module#MultimediaAnnotatorSourceDataTypeModule'
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
