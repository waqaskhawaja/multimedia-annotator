/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { AnalystUpdateComponent } from 'app/entities/analyst/analyst-update.component';
import { AnalystService } from 'app/entities/analyst/analyst.service';
import { Analyst } from 'app/shared/model/analyst.model';

describe('Component Tests', () => {
    describe('Analyst Management Update Component', () => {
        let comp: AnalystUpdateComponent;
        let fixture: ComponentFixture<AnalystUpdateComponent>;
        let service: AnalystService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [AnalystUpdateComponent]
            })
                .overrideTemplate(AnalystUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AnalystUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalystService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Analyst(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.analyst = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Analyst();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.analyst = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
