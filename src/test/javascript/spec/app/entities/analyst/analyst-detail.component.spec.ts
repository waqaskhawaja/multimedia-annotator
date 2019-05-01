/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { AnalystDetailComponent } from 'app/entities/analyst/analyst-detail.component';
import { Analyst } from 'app/shared/model/analyst.model';

describe('Component Tests', () => {
    describe('Analyst Management Detail Component', () => {
        let comp: AnalystDetailComponent;
        let fixture: ComponentFixture<AnalystDetailComponent>;
        const route = ({ data: of({ analyst: new Analyst(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [AnalystDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AnalystDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnalystDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.analyst).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
