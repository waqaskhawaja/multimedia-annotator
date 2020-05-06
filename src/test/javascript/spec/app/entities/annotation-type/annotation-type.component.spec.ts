import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MaTestModule } from '../../../test.module';
import { AnnotationTypeComponent } from 'app/entities/annotation-type/annotation-type.component';
import { AnnotationTypeService } from 'app/entities/annotation-type/annotation-type.service';
import { AnnotationType } from 'app/shared/model/annotation-type.model';

describe('Component Tests', () => {
    describe('AnnotationType Management Component', () => {
        let comp: AnnotationTypeComponent;
        let fixture: ComponentFixture<AnnotationTypeComponent>;
        let service: AnnotationTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnnotationTypeComponent],
                providers: []
            })
                .overrideTemplate(AnnotationTypeComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AnnotationTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnnotationTypeService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new AnnotationType(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.annotationTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
