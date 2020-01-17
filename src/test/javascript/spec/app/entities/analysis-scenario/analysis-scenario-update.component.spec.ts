/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { AnalysisScenarioUpdateComponent } from 'app/entities/analysis-scenario/analysis-scenario-update.component';
import { AnalysisScenarioService } from 'app/entities/analysis-scenario/analysis-scenario.service';
import { AnalysisScenario } from 'app/shared/model/analysis-scenario.model';

describe('Component Tests', () => {
    describe('AnalysisScenario Management Update Component', () => {
        let comp: AnalysisScenarioUpdateComponent;
        let fixture: ComponentFixture<AnalysisScenarioUpdateComponent>;
        let service: AnalysisScenarioService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnalysisScenarioUpdateComponent]
            })
                .overrideTemplate(AnalysisScenarioUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AnalysisScenarioUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalysisScenarioService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new AnalysisScenario(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.analysisScenario = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new AnalysisScenario();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.analysisScenario = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
