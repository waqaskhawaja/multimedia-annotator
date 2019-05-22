/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { AnalysisSessionResourceDetailComponent } from 'app/entities/analysis-session-resource/analysis-session-resource-detail.component';
import { AnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';

describe('Component Tests', () => {
    describe('AnalysisSessionResource Management Detail Component', () => {
        let comp: AnalysisSessionResourceDetailComponent;
        let fixture: ComponentFixture<AnalysisSessionResourceDetailComponent>;
        const route = ({ data: of({ analysisSessionResource: new AnalysisSessionResource(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnalysisSessionResourceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AnalysisSessionResourceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnalysisSessionResourceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.analysisSessionResource).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
