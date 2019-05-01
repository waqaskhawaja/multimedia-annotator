/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { ScenarioComponent } from 'app/entities/scenario/scenario.component';
import { ScenarioService } from 'app/entities/scenario/scenario.service';
import { Scenario } from 'app/shared/model/scenario.model';

describe('Component Tests', () => {
    describe('Scenario Management Component', () => {
        let comp: ScenarioComponent;
        let fixture: ComponentFixture<ScenarioComponent>;
        let service: ScenarioService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [ScenarioComponent],
                providers: []
            })
                .overrideTemplate(ScenarioComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ScenarioComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ScenarioService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Scenario(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.scenarios[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
