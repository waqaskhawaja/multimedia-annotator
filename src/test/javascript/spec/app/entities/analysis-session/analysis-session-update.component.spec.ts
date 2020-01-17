/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { AnalysisSessionUpdateComponent } from 'app/entities/analysis-session/analysis-session-update.component';
import { AnalysisSessionService } from 'app/entities/analysis-session/analysis-session.service';
import { AnalysisSession } from 'app/shared/model/analysis-session.model';

describe('Component Tests', () => {
    describe('AnalysisSession Management Update Component', () => {
        let comp: AnalysisSessionUpdateComponent;
        let fixture: ComponentFixture<AnalysisSessionUpdateComponent>;
        let service: AnalysisSessionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnalysisSessionUpdateComponent]
            })
                .overrideTemplate(AnalysisSessionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AnalysisSessionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalysisSessionService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new AnalysisSession(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.analysisSession = entity;
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
                    const entity = new AnalysisSession();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.analysisSession = entity;
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
