/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { AnalysisSessionDetailComponent } from 'app/entities/analysis-session/analysis-session-detail.component';
import { AnalysisSession } from 'app/shared/model/analysis-session.model';

describe('Component Tests', () => {
    describe('AnalysisSession Management Detail Component', () => {
        let comp: AnalysisSessionDetailComponent;
        let fixture: ComponentFixture<AnalysisSessionDetailComponent>;
        const route = ({ data: of({ analysisSession: new AnalysisSession(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnalysisSessionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AnalysisSessionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnalysisSessionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.analysisSession).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
