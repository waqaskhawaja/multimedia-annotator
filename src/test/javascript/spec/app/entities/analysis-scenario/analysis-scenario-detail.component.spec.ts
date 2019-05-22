/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { AnalysisScenarioDetailComponent } from 'app/entities/analysis-scenario/analysis-scenario-detail.component';
import { AnalysisScenario } from 'app/shared/model/analysis-scenario.model';

describe('Component Tests', () => {
    describe('AnalysisScenario Management Detail Component', () => {
        let comp: AnalysisScenarioDetailComponent;
        let fixture: ComponentFixture<AnalysisScenarioDetailComponent>;
        const route = ({ data: of({ analysisScenario: new AnalysisScenario(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnalysisScenarioDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AnalysisScenarioDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnalysisScenarioDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.analysisScenario).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
