/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { AnalystComponent } from 'app/entities/analyst/analyst.component';
import { AnalystService } from 'app/entities/analyst/analyst.service';
import { Analyst } from 'app/shared/model/analyst.model';

describe('Component Tests', () => {
    describe('Analyst Management Component', () => {
        let comp: AnalystComponent;
        let fixture: ComponentFixture<AnalystComponent>;
        let service: AnalystService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [AnalystComponent],
                providers: []
            })
                .overrideTemplate(AnalystComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AnalystComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalystService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Analyst(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.analysts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
