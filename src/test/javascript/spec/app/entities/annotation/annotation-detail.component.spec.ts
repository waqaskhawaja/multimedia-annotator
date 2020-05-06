import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { AnnotationDetailComponent } from 'app/entities/annotation/annotation-detail.component';
import { Annotation } from 'app/shared/model/annotation.model';

describe('Component Tests', () => {
    describe('Annotation Management Detail Component', () => {
        let comp: AnnotationDetailComponent;
        let fixture: ComponentFixture<AnnotationDetailComponent>;
        const route = ({ data: of({ annotation: new Annotation(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnnotationDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AnnotationDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnnotationDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.annotation).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
