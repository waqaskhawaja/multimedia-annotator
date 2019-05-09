/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { AnnotationSessionUpdateComponent } from 'app/entities/annotation-session/annotation-session-update.component';
import { AnnotationSessionService } from 'app/entities/annotation-session/annotation-session.service';
import { AnnotationSession } from 'app/shared/model/annotation-session.model';

describe('Component Tests', () => {
    describe('AnnotationSession Management Update Component', () => {
        let comp: AnnotationSessionUpdateComponent;
        let fixture: ComponentFixture<AnnotationSessionUpdateComponent>;
        let service: AnnotationSessionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnnotationSessionUpdateComponent]
            })
                .overrideTemplate(AnnotationSessionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AnnotationSessionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnnotationSessionService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new AnnotationSession(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.annotationSession = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new AnnotationSession();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.annotationSession = entity;
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
