/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { AnnotationSessionDetailComponent } from 'app/entities/annotation-session/annotation-session-detail.component';
import { AnnotationSession } from 'app/shared/model/annotation-session.model';

describe('Component Tests', () => {
    describe('AnnotationSession Management Detail Component', () => {
        let comp: AnnotationSessionDetailComponent;
        let fixture: ComponentFixture<AnnotationSessionDetailComponent>;
        const route = ({ data: of({ annotationSession: new AnnotationSession(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnnotationSessionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AnnotationSessionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnnotationSessionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.annotationSession).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
