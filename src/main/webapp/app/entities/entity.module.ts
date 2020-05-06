import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'resource-type',
                loadChildren: './resource-type/resource-type.module#MaResourceTypeModule'
            },
            {
                path: 'analysis-scenario',
                loadChildren: './analysis-scenario/analysis-scenario.module#MaAnalysisScenarioModule'
            },
            {
                path: 'analysis-session',
                loadChildren: './analysis-session/analysis-session.module#MaAnalysisSessionModule'
            },
            {
                path: 'analysis-session-resource',
                loadChildren: './analysis-session-resource/analysis-session-resource.module#MaAnalysisSessionResourceModule'
            },
            {
                path: 'interaction-record',
                loadChildren: './interaction-record/interaction-record.module#MaInteractionRecordModule'
            },
            {
                path: 'annotation-session',
                loadChildren: './annotation-session/annotation-session.module#MaAnnotationSessionModule'
            },
            {
                path: 'annotation',
                loadChildren: './annotation/annotation.module#MaAnnotationModule'
            },
            {
                path: 'interaction-type',
                loadChildren: './interaction-type/interaction-type.module#MaInteractionTypeModule'
            },
            {
                path: 'analysis-session',
                loadChildren: './analysis-session/analysis-session.module#MaAnalysisSessionModule'
            },
            {
                path: 'analysis-session-resource',
                loadChildren: './analysis-session-resource/analysis-session-resource.module#MaAnalysisSessionResourceModule'
            },
            {
                path: 'annotation-type',
                loadChildren: () => import('./annotation-type/annotation-type.module').then(m => m.MaAnnotationTypeModule)
            },
            {
                path: 'data-set',
                loadChildren: () => import('./data-set/data-set.module').then(m => m.MaDataSetModule)
            },
            {
                path: 'data-set-resource',
                loadChildren: () => import('./data-set-resource/data-set-resource.module').then(m => m.MaDataSetResourceModule)
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MaEntityModule {}
